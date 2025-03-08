package com.ims.IMS.controller.admin.crud_device;


import com.ims.IMS.api.device.DeleteDeviceResponse;
import com.ims.IMS.api.device.DeviceRegisterRequest;
import com.ims.IMS.api.device.DeviceRegisterResponse;
import com.ims.IMS.api.device.ListDeviceResponse;
import com.ims.IMS.api.device.UpdateDeviceRequest;
import com.ims.IMS.api.device.UpdateDeviceResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.lib.api.ResponseStatus;
import com.ims.IMS.mapper.ImsMapping;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.CusDev.CustomerDevice;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.imsprocessing.Device;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.CustomerDeviceRepository;
import com.ims.IMS.repository.CustomerRepository;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.repository.LocationRepository;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ims/admin")
public class DeviceCrud {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationAllService authenticationAllService;
    @Autowired
    private AuthenticationCustomerService authenticationCustomerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DeviceRepository deviceRepository;
    private final ImsMapping imsMapping;
    @Autowired
    public DeviceCrud(@Qualifier("imsMappingImpl") ImsMapping imsMapping) {
        this.imsMapping = imsMapping;
    }
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/list-device")
    public ResponseApi<List<ListDeviceResponse>> getListDevice(@RequestHeader("Authorization") String token) {
        // Validate the token and get admin data
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Fetch all customers from the repository
        List<Customer> customers = customerRepository.findAll();
        // Fetch all customer-device associations
        List<CustomerDevice> customerDevices = customerDeviceRepository.findAll();
        // Fetch all devices from the repository
        List<Device> devices = deviceRepository.findAll();
        // Create a map for quick lookup of customer details by email
        Map<String, Customer> emailToCustomerMap = customers.stream()
                .collect(Collectors.toMap(Customer::getEmail, customer -> customer));
        // Create a map for quick lookup of device IDs to customer emails
        Map<String, String> deviceIdToEmailMap = customerDevices.stream()
                .flatMap(cd -> cd.getDeviceID().stream()
                        .map(deviceID -> new AbstractMap.SimpleEntry<>(deviceID, cd.getEmail())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // Map devices to ListDeviceResponse based on the customer-device association
        List<ListDeviceResponse> listDevices = devices.stream()
                .map(device -> {
                    // Find the customer email associated with the device
                    String customerEmail = deviceIdToEmailMap.get(device.getDeviceID());
                    // Find the customer details using the email
                    Customer correspondingCustomer = emailToCustomerMap.get(customerEmail);
                    // Map device to ListDeviceResponse
                    if (correspondingCustomer != null) {
                        return imsMapping.mapFromDevice(
                                device,
                                correspondingCustomer.getSecret_key(),
                                correspondingCustomer.getEmail(),
                                correspondingCustomer.getPhoneNumber()
                        );
                    }
                    // If no customer or association is found, handle it with default values
                    return imsMapping.mapFromDevice(device, "N/A", "N/A", "N/A");
                })
                .collect(Collectors.toList());
        // Return the mapped list as a successful response
        return ResponseApi.success(listDevices);
    }

    @PostMapping("/create-device")
    public ResponseApi<DeviceRegisterResponse> createDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody DeviceRegisterRequest deviceRegisterRequest) {
        try {
            Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
            // Check if a device with the same deviceID already exists
            if (deviceRepository.existsByDeviceID(deviceRegisterRequest.deviceID())) {
                ResponseStatus status = new ResponseStatus("400", "Device ID already exists");
                return ResponseApi.success(status, null, null);
            }
            Device deviceRegister = Device.fromAdminCreate(deviceRegisterRequest);
            deviceRepository.save(deviceRegister);
            DeviceRegisterResponse deviceRegisterResponse = transformToCustomerRegisterResponse(deviceRegister);
            return ResponseApi.success(deviceRegisterResponse);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus status = new ResponseStatus("400", "Email and Phone Number already exists");
            return ResponseApi.success(status, null, null);
        } catch (LoginWrongRole e) {
            ResponseStatus status = new ResponseStatus("403", "Login with wrong role");
            return ResponseApi.success(status, null, null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.success(status, null, null);
        }
    }

    @DeleteMapping("/delete-device/{deviceID}")
    public ResponseApi<DeleteDeviceResponse> deleteDevice(
            @RequestHeader("Authorization") String token,
            @PathVariable String deviceID) {
        try {
            // Validate the token and get admin data
            Admin adminData = adminService.validateTokenAndGetAdmin(token);
            // Fetch the device by its ID
            Optional<Device> deviceOptional = deviceRepository.findByDeviceID(deviceID);
            if (deviceOptional.isEmpty()) {
                // Device with the specified ID does not exist
                ResponseStatus status = new ResponseStatus("404", "Device not found");
                return ResponseApi.success(status, null, null);
            }
            // Device exists, delete it
            Device device = deviceOptional.get();
            deviceRepository.delete(device);
            // Prepare the response
            DeleteDeviceResponse deleteDeviceResponse = new DeleteDeviceResponse("Device Deleted Successfully");
            return ResponseApi.success(deleteDeviceResponse);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus status = new ResponseStatus("400", "Email and Phone Number already exists");
            return ResponseApi.success(status, null, null);
        } catch (LoginWrongRole e) {
            ResponseStatus status = new ResponseStatus("403", "Login with wrong role");
            return ResponseApi.success(status, null, null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.success(status, null, null);
        }
    }

    @PutMapping("/update-device")
    public ResponseApi<UpdateDeviceResponse> updateDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateDeviceRequest updateDeviceRequest) {
        try {
            // Validate the token and get admin data
            Admin adminData = adminService.validateTokenAndGetAdmin(token);
            // Fetch the device by its ID
            Optional<Device> deviceOptional = deviceRepository.findByDeviceID(updateDeviceRequest.deviceID());
            if (deviceOptional.isEmpty()) {
                // Device with the specified ID does not exist
                ResponseStatus status = new ResponseStatus("404", "Device not found");
                return ResponseApi.success(status, null, null);
            }
            // Device exists, update its details
            Device device = deviceOptional.get();
            device.setDescription(updateDeviceRequest.description());
            device.setUpdated_at(updateDeviceRequest.updated_at());
            device.setMissionDescription(updateDeviceRequest.missionDescription());
            device.setLocationX(updateDeviceRequest.locationX());
            device.setLocationY(updateDeviceRequest.locationY());
            device.setLocationID(updateDeviceRequest.locationID());
            // Save the updated device
            Device updatedDevice = deviceRepository.save(device);
            // Prepare the response
            UpdateDeviceResponse updateDeviceResponse = new UpdateDeviceResponse("Device Updated Successfully", updatedDevice);
            return ResponseApi.success(updateDeviceResponse);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus status = new ResponseStatus("400", "Device ID already exists");
            return ResponseApi.success(status, null, null);
        } catch (LoginWrongRole e) {
            ResponseStatus status = new ResponseStatus("403", "Login with wrong role");
            return ResponseApi.success(status, null, null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.success(status, null, null);
        }
    }

    private DeviceRegisterResponse transformToCustomerRegisterResponse(Device device) {
        return new DeviceRegisterResponse(device.getDeviceID(), device.getDescription(),"OK");
    }
}

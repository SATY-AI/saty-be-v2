package com.ims.IMS.controller.customer.crud_device;

import com.ims.IMS.api.CustomerListDeviceResponse;
import com.ims.IMS.api.CustomerRegisterDeviceRequest;
import com.ims.IMS.api.CustomerRegisterDeviceResponse;
import com.ims.IMS.api.image.ImageDeviceRequest;
import com.ims.IMS.api.image.ImageDeviceResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.DatabaseException;
import com.ims.IMS.exception.InvalidTokenException;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.lib.api.ResponseStatus;
import com.ims.IMS.model.CusDev.CustomerDevice;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.imsprocessing.Device;
import com.ims.IMS.model.imsprocessing.Image;
import com.ims.IMS.repository.CustomerDeviceRepository;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.repository.Image.ImageRepository;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.qrCodeService.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ims/customer")
public class DeviceCustomerCrud {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationAllService authenticationAllService;
    @Autowired
    private AuthenticationCustomerService authenticationCustomerService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private QrCodeService qrCodeService;


    @PostMapping("/register-device")
    public ResponseApi<CustomerRegisterDeviceResponse> registerDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody CustomerRegisterDeviceRequest customerRegisterDeviceRequest
    ) {
        try {
            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);

            // Check if the customer with the provided email exists
            Optional<CustomerDevice> customerDeviceOptional = customerDeviceRepository.findByEmail(customerRegisterDeviceRequest.email());
            CustomerDevice customerDevice;
            if (customerDeviceOptional.isEmpty()) {
                // If the email does not exist, create a new CustomerDevice with an empty device set
                customerDevice = new CustomerDevice();
                customerDevice.setEmail(customerRegisterDeviceRequest.email());
                customerDevice.setDeviceID(new HashSet<>()); // Initialize with an empty set
                customerDeviceRepository.save(customerDevice);
            } else {
                // If the email exists, retrieve the CustomerDevice
                customerDevice = customerDeviceOptional.get();
            }

            // Separate the device IDs into those that exist and those that do not
            List<String> existingDeviceIDs = new ArrayList<>();
            List<String> nonExistingDeviceIDs = new ArrayList<>();
            for (String deviceID : customerRegisterDeviceRequest.deviceID()) {
                if (deviceID == null || deviceID.trim().isEmpty()) {
                    // Handle invalid device ID format
                    throw new IllegalArgumentException("Device ID cannot be null or empty");
                }
                if (deviceRepository.existsByDeviceID(deviceID)) {
                    existingDeviceIDs.add(deviceID);
                } else {
                    nonExistingDeviceIDs.add(deviceID);
                }
            }

            // Add only the existing device IDs to the customer's device set
            HashSet<String> customerDeviceIDs = customerDevice.getDeviceID();
            if (customerDeviceIDs == null) {
                customerDeviceIDs = new HashSet<>();
            }
            customerDeviceIDs.addAll(existingDeviceIDs);
            customerDevice.setDeviceID(customerDeviceIDs);
            customerDeviceRepository.save(customerDevice);

            // Prepare the response
            CustomerRegisterDeviceResponse response = new CustomerRegisterDeviceResponse(
                    "Device registration completed",
                    customerDevice.getEmail(),
                    existingDeviceIDs,
                    nonExistingDeviceIDs
            );
            return ResponseApi.success(response);
        } catch (IllegalArgumentException e) {
            // Handle invalid request data
            ResponseStatus status = new ResponseStatus("400", e.getMessage());
            return ResponseApi.error(status.code(), status.message());
        } catch (LoginWrongRole e) {
            ResponseStatus status = new ResponseStatus("403", "Login with wrong role");
            return ResponseApi.error(status.code(), status.message());
        } catch (DatabaseException e) {
            // Handle database exceptions
            ResponseStatus status = new ResponseStatus("500", "Database Error");
            return ResponseApi.error(status.code(), status.message());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.error(status.code(), status.message());
        }
    }

    @GetMapping("/get-list-device")
    public ResponseApi<List<CustomerListDeviceResponse>> getListDevice(
            @RequestHeader("Authorization") String token) {
        try {
            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);
            // Find the CustomerDevice entry based on the customer's email
            Optional<CustomerDevice> customerDeviceOptional = customerDeviceRepository.findByEmail(customerData.getEmail());
            if (customerDeviceOptional.isEmpty()) {
                ResponseStatus status = new ResponseStatus("404", "Customer Device not Found!");
                return ResponseApi.success(status, null, null);
            }
            // Extract the list of device IDs from the CustomerDevice entry
            CustomerDevice customerDevice = customerDeviceOptional.get();
            Set<String> deviceIDs = customerDevice.getDeviceID(); // Assuming Set<String> for uniqueness
            // Prepare the response list
            List<CustomerListDeviceResponse> deviceResponses = new ArrayList<>();
            // Fetch each device's details and add to the response list
            for (String deviceID : deviceIDs) {
                Optional<Device> deviceOptional = deviceRepository.findByDeviceID(deviceID);
                if (deviceOptional.isPresent()) {
                    Device device = deviceOptional.get();
                    String email = customerData.getEmail();
                    String secretKey = customerData.getSecret_key();
                    String phoneNumber = customerData.getPhoneNumber();
                    // Generate QR code for the device ID
                    String qrCodeBase64 = qrCodeService.generateQRCodeBase64(secretKey, deviceID, email, phoneNumber);
                    CustomerListDeviceResponse response = new CustomerListDeviceResponse(
                            device.getDeviceID(),
                            device.getDescription(),
                            device.getUpdated_at(),
                            device.getMissionDescription(),
                            device.getLocationX(),
                            device.getLocationY(),
                            device.getLocationID(),
                            secretKey,
                            qrCodeBase64
                    );
                    deviceResponses.add(response);
                }
            }

            // Return the response
            return ResponseApi.success(deviceResponses);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus status = new ResponseStatus("400", "Email and Phone Number already exist");
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

    @PostMapping("/get-detail-device")
    public ResponseApi<List<ImageDeviceResponse>> getImageDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody ImageDeviceRequest imageDeviceRequest
    ) {
        try {
            String urlS3 = "https://saty-dataset.s3.ap-southeast-1.amazonaws.com";
            // Validate the token and get customer data
            Customer customerData = customerService.validateTokenAndGetCustomer(token);
            // Check if the customer data is valid
            if (customerData == null) {
                return ResponseApi.error("401", "Invalid or expired token");
            }

            // Validate that the deviceID is provided and not empty
            if (imageDeviceRequest.deviceID() == null || imageDeviceRequest.deviceID().isEmpty()) {
                return ResponseApi.error("400", "Device ID must be provided");
            }
            // Fetch the list of images for the provided deviceID
            List<Image> images = imageRepository.findByDeviceID(imageDeviceRequest.deviceID());
            // Check if the list is empty, meaning the deviceID does not exist
            if (images.isEmpty()) {
                return ResponseApi.error("404", "No images found for the provided deviceID");
            }
            // Convert the list of Image entities to ImageDeviceResponse records
            List<ImageDeviceResponse> responseList = images.stream()
                    .map(image -> new ImageDeviceResponse(
                            urlS3,
                            image.getImageBucket(),
                            image.getDescription(),
                            image.getTopic(),
                            image.getDeviceID(),
                            image.getImageList()
                    ))
                    .collect(Collectors.toList());
            // Return the successful response with the list of images
            return ResponseApi.success(responseList);
        } catch (InvalidTokenException e) {
            return ResponseApi.error("401", "Invalid or expired token");
        } catch (IllegalArgumentException e) {
            return ResponseApi.error("400", "Bad request: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseApi.error("404", "No data found: " + e.getMessage());
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseApi.error("500", "Internal server error");
        }
    }






}

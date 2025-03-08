package com.ims.IMS.controller.admin.crud_customer;

import com.ims.IMS.api.CustomerDeviceResponse;
import com.ims.IMS.api.CustomerRegisterRequest;
import com.ims.IMS.api.CustomerRegisterResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.lib.api.ResponseStatus;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.groupuser.AuthenticationResponse;
import com.ims.IMS.model.CusDev.CustomerDevice;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.dto.AllInformation;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.CustomerDeviceRepository;
import com.ims.IMS.repository.CustomerRepository;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ims/admin")
public class CustomerCrud {
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
    @Lazy
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/list-customer")
    public ResponseApi<List<AllInformation>> getListCustomer(@RequestHeader("Authorization") String token) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        List<AllInformation> allInformationList = new ArrayList<>();
        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            AllInformation allInformation = AllInformation.fromUser(customer);
            allInformationList.add(allInformation);
        }
        return ResponseApi.success(allInformationList);
    }

    @DeleteMapping("/delete-customer/{customerId}")
    public ResponseApi<?> deleteCustomer(@RequestHeader("Authorization") String token,
                                         @PathVariable Integer customerId) {
        // Validate the admin token
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Perform the deletion operation
        boolean deletionSuccessful = customerService.deleteCustomerById(customerId); // Assuming this method exists
        // Check if the deletion was successful and return appropriate response
        if (deletionSuccessful) {
            // String concatenation
            return ResponseApi.success("Customer ID: " + customerId + " deleted successfully");
        } else {
            return ResponseApi.error("400", "Failed to delete customer with ID: " + customerId);
        }
    }

    @PostMapping("/create-customer")
    public ResponseApi<CustomerRegisterResponse> createCustomer(
            @RequestHeader("Authorization") String token,
            @RequestBody CustomerRegisterRequest customerRegisterRequest) {
        try {
            Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
            Customer customerRegister = Customer.fromAdminCreate(customerRegisterRequest);
            AuthenticationResponse customerResponse = authenticationCustomerService.register(customerRegister);
            CustomerRegisterResponse customerRegisterResponse = transformToCustomerRegisterResponse(customerResponse);
            return ResponseApi.success(customerRegisterResponse);
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

    //Disable and enable
    @PutMapping("/disable-customer/{customerId}")
    public ResponseApi<Map<String, String>> disableCustomer(@PathVariable Integer customerId) {
        boolean updateSuccessful = customerService.disableCustomer(customerId);
        if (updateSuccessful) {
            return ResponseApi.success(Map.of("Message", "Customer ID: " + customerId + " disabled successfully"));
        } else {
            return ResponseApi.error("400", "Customer update failed. The specified Customer was not found.");
        }
    }

    @PutMapping("/enable-customer/{customerId}")
    public ResponseApi<Map<String, String>> enableCustomer(@PathVariable Integer customerId) {
        boolean updateSuccessful = customerService.enableCustomer(customerId);
        if (updateSuccessful) {
            return ResponseApi.success(Map.of("Message", "Customer ID: " + customerId + " enabled successfully"));
        } else {
            return ResponseApi.error("400", "Customer update failed. The specified Customer was not found.");
        }
    }

    @PutMapping("/update-customer")
    public ResponseApi<Map<String, Object>> updateCustomer(@RequestHeader("Authorization") String token,
                                                           @RequestBody Customer customer) {
        adminService.validateTokenAndGetAdmin(token);
        if (customerService.checkUserExistence(customer.getEmail())) {
            boolean updateSuccessful = customerService.updateCustomerInformation(customer);
            if (updateSuccessful) {
                return ResponseApi.success(Map.of("status", "success", "message", "Customer with Email " + customer.getEmail() + " updated"));
            } else {
                return ResponseApi.error("404", "Customer Update Fail");
            }
        } else {
            return ResponseApi.error("404", "Customer No Existence || Customer InActive");
        }
    }

    @GetMapping("/get-list-customer-device")
    public ResponseApi<List<CustomerDeviceResponse>> getListCustomerDevice(
            @RequestHeader("Authorization") String token) {
        try {
            // Validate the admin token and ensure they have the right permissions
            adminService.validateTokenAndGetAdmin(token);
            // Fetch all customer devices from the repository
            List<CustomerDevice> customerDevices = customerDeviceRepository.findAll();

            // Transform the data into the response format
            List<CustomerDeviceResponse> responseList = customerDevices.stream()
                    .map(device -> new CustomerDeviceResponse(device.getEmail(), new ArrayList<>(device.getDeviceID())))
                    .collect(Collectors.toList());
            // Return the response list
            return ResponseApi.success(responseList);
        } catch (LoginWrongRole e) {
            ResponseStatus status = new ResponseStatus("403", "Login with wrong role");
            return ResponseApi.success(status, null, null);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ResponseStatus status = new ResponseStatus("500", "Internal server error");
            return ResponseApi.success(status, null, null);
        }
    }



    private CustomerRegisterResponse transformToCustomerRegisterResponse(AuthenticationResponse authResponse) {
        return new CustomerRegisterResponse(authResponse.getToken(), "OK");
    }

}

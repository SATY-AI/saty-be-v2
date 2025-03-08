package com.ims.IMS.controller.customer;

import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.groupuser.CustomerUpdate;
import com.ims.IMS.model.dto.CustomerInformation;
import com.ims.IMS.repository.CustomerRepository;
import com.ims.IMS.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationCustomerService authenticationCustomerService;
    @Autowired
    private CustomerService customerService;


    @GetMapping("/getCustomer")
    public ResponseApi<CustomerInformation> getUserFromToken(@RequestHeader("Authorization") String token) {
        String jwtToken = jwtService.extractToken(token);
        Customer customer = jwtService.decodeTokenCustomer(jwtToken);
        Customer customerData = customerRepository.findByEmailAndRole(customer.getEmail(), customer.getRole());
        String getRole = String.valueOf(customer.getRole());
        if (customerData != null && customerService.checkUserRole(getRole)) {
            // Convert User entity to UserDTO
            CustomerInformation userDTO = CustomerInformation.fromUser(customerData);
            return ResponseApi.success(userDTO);
        } else {
            // Handle the case where no user matching the criteria is found
            return ResponseApi.error("01", "NOT FOUND");
        }
    }

    @PutMapping("/updateCustomer")
    public ResponseEntity<?> updateCustomer(@RequestHeader("Authorization") String token,
                                            @RequestBody CustomerUpdate customerUpdate) {
        String jwtToken = jwtService.extractToken(token);
        Customer customerInfo = jwtService.decodeTokenCustomer(jwtToken);
        Customer customerData = customerRepository.findByEmailAndRole(customerInfo.getEmail(), customerInfo.getRole());
        String getRole = String.valueOf(customerInfo.getRole());
        if (customerData != null && customerService.checkUserRole(getRole)) {
            if (customerService.checkUserExistence(customerInfo.getEmail())) {
                if (customerService.updateCustomerInfoFromCustomer(customerUpdate)) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Customer with Email " + customerUpdate.getEmail() + " updated");
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "error");
                    response.put("message", "Customer Update Fail");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Customer No Existence");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            // Create a response map with a message and status code
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "User not found or unauthorized.");
            // Return the response with a 404 status code
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}


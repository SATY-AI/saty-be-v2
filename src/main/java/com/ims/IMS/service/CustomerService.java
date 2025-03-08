package com.ims.IMS.service;

import com.ims.IMS.config.JwtService;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.groupuser.CustomerUpdate;
import com.ims.IMS.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // Validate the JWT token and fetch the corresponding customer
    public Customer validateTokenAndGetCustomer(String token) {
        // Extract the JWT token from the Authorization header
        String jwtToken = jwtService.extractToken(token);

        // Decode the token to get the Customer details
        Customer customer = jwtService.decodeTokenCustomer(jwtToken);
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        // Fetch the customer data from the repository
        Customer customerData = customerRepository.findByEmailAndRole(customer.getEmail(), customer.getRole());
        if (customerData == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customer not found or invalid role");
        }

        // Ensure the customer has a valid role
        if (!checkUserRole(String.valueOf(customer.getRole()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized role");
        }

        // Return the validated customer data
        return customerData;
    }

    // Update customer information (Admin Update)
    public Boolean updateCustomerInformation(Customer customer) {
        Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());
        if (existingCustomer != null) {
            existingCustomer.setUsername(customer.getUsername());
            existingCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setRole(customer.getRole());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setUpdatedAt(ZonedDateTime.now());
            existingCustomer.setSecret_key(customer.getSecret_key());
            customerRepository.save(existingCustomer);
            return true;
        } else {
            return false;
        }
    }

    // Update customer information based on customer request (Self Update)
    public Boolean updateCustomerInfoFromCustomer(CustomerUpdate customerUpdate) {
        Customer existingCustomer = customerRepository.findByEmail(customerUpdate.getEmail());
        if (existingCustomer != null) {
            existingCustomer.setFullName(customerUpdate.getFullName());
            existingCustomer.setEmail(customerUpdate.getEmail());
            existingCustomer.setPhoneNumber(customerUpdate.getPhoneNumber());
            existingCustomer.setPassword(passwordEncoder.encode(customerUpdate.getPassword()));
            existingCustomer.setStatusActive(customerUpdate.getStatusactive());
            customerRepository.save(existingCustomer);
            return true;
        } else {
            return false;
        }
    }

    // Check if a customer exists by email and is active
    public boolean checkUserExistence(String customerEmail) {
        Optional<Customer> existingCustomer = Optional.ofNullable(customerRepository.findByEmail(customerEmail));
        return existingCustomer.isPresent() && existingCustomer.get().getStatusActive();
    }

    // Validate the user's role
    public boolean checkUserRole(String role) {
        return role.equals("CUSTOMER");
    }

    // Delete customer by ID
    public boolean deleteCustomerById(Integer customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            customerRepository.deleteById(customerId);
            return true;
        } else {
            return false;
        }
    }

    // Disable customer (mark as inactive)
    public boolean disableCustomer(Integer customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setStatusActive(false);
            customer.setUpdatedAt(ZonedDateTime.now());
            customerRepository.save(customer);
            return true;
        } else {
            return false;
        }
    }

    // Enable customer (mark as active)
    public boolean enableCustomer(Integer customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setStatusActive(true);
            customer.setUpdatedAt(ZonedDateTime.now());
            customerRepository.save(customer);
            return true;
        } else {
            return false;
        }
    }
}

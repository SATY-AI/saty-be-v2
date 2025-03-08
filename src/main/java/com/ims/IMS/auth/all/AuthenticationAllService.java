package com.ims.IMS.auth.all;

import com.ims.IMS.common.Role;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.StatusNotActive;
import com.ims.IMS.exception.WrongInformation;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.groupuser.AuthenticationResponse;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationAllService {
    @Autowired
    private final AdminRepository adminRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private final JwtService jwtService;


    public String determineRoleandEmail(String email, Role role) {
        if (adminRepository.findByEmail(email) != null && role.equals(Role.ADMIN)) {
            return "admin";
        } else if (customerRepository.findByEmail(email) != null && role.equals(Role.CUSTOMER)) {
            return "customer";
        }
        // Return null or throw an exception if the email does not belong to any role
        return null;
    }

    public Role determineByRole(Role role) {
        if (role.equals(Role.CUSTOMER)) {
            return Role.CUSTOMER;
        } else if (role.equals(Role.ADMIN)) {
            return Role.ADMIN;
        }
        return null;
    }

    public AuthenticationResponse authenticateByEmailAndPassword(String email, String password, String role) {
        switch (role) {
            case "admin":
                Admin admin = adminRepository.findByEmail(email);
                if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
                    var jwtToken = jwtService.generateTokenAdmin(admin);
                    return AuthenticationResponse.builder().token(jwtToken).build();
                }
                break;
            case "customer":
                Customer customer = customerRepository.findByEmail(email);
                if (customer != null && passwordEncoder.matches(password, customer.getPassword()) && customer.getStatusActive().equals(true)) {
                    var jwtToken = jwtService.generateToken(customer);
                    return AuthenticationResponse.builder().token(jwtToken).build();
                } else if (customer == null) {
                    // Authentication failed, return null or handle as needed
                    throw new WrongInformation("Information is not existence, please register");
                } else {
                    throw new StatusNotActive("Status Not Active");
                }
            default:
                // Handle invalid role
                throw new IllegalArgumentException("Invalid role");
        }

        // Authentication failed, return null or handle as needed
        throw new WrongInformation("Information is not existence, please register");
    }

}

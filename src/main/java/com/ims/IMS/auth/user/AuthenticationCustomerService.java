package com.ims.IMS.auth.user;

import com.ims.IMS.common.Role;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.NotExistenceData;
import com.ims.IMS.exception.StatusNotActive;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.exception.WrongInformation;
import com.ims.IMS.model.groupuser.AuthenticationRequest;
import com.ims.IMS.model.groupuser.AuthenticationResponse;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.groupuser.User;
import com.ims.IMS.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthenticationCustomerService {
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtService jwtService;

    private String generateRandomSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];  // 16 bytes for a 128-bit key
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);  // Encode in Base64 to make it a readable string
    }

    @Transactional
    public AuthenticationResponse register(Customer customerRegister) {
        if (customerRegister.getRole() == Role.CUSTOMER) {
            if (isUserExistsByUsername(customerRegister.getUsername()) || isUserExistsByPhoneNumber(customerRegister.getPhoneNumber())) {
                throw new UsernameAlreadyExistsException("Email and PhoneNumber already exists");
            }
            String secretKey = generateRandomSecretKey();
            // Username is unique, proceed with user registration
            Customer customer = Customer.builder()
                    .fullName(customerRegister.getFullName())
                    .email(customerRegister.getEmail())
                    .phoneNumber(customerRegister.getPhoneNumber())
                    .password(passwordEncoder.encode(customerRegister.getPassword()))
                    .statusActive(true)
                    .role(Role.CUSTOMER)
                    .createdAt(ZonedDateTime.now())
                    .updatedAt(ZonedDateTime.now())
                    .secret_key(secretKey)
                    .build();
            customerRepository.save(customer);
            var jwtToken = jwtService.generateTokenCustomer(customer);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } else {
            throw new LoginWrongRole("Login with wrong role");
        }
    }

    public AuthenticationResponse authenticateByEmailAndPassword(String email, String password) {
        // Fetch the customer by email
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            // Customer with the provided email does not exist
            throw new NotExistenceData("User with the provided email does not exist, please register");
        }

        if (!customer.getStatusActive()) {
            // Customer account is not active
            throw new StatusNotActive("Account is not active. Please contact support.");
        }

        // Check if the provided password matches
        if (!passwordEncoder.matches(password, customer.getPassword())) {
            // Provided password is incorrect
            throw new WrongInformation("Incorrect password provided. Please try again.");
        }

        // If all checks pass, generate and return the JWT token
        var jwtToken = jwtService.generateTokenCustomer(customer);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    private boolean isUserExistsByUsername(String email) {
        User existingUser = customerRepository.findByEmail(email);
        return existingUser != null;
    }

    private boolean isUserExistsByPhoneNumber(String phonenumber) {
        User existingUser = customerRepository.findByPhoneNumber(phonenumber);
        return existingUser != null;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = customerRepository.findByEmail(request.getEmail());
        var jwtToken = jwtService.generateTokenCustomer(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}

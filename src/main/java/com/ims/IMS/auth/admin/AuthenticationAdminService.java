package com.ims.IMS.auth.admin;

import com.ims.IMS.common.Role;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.exception.WrongInformation;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.groupuser.AuthenticationResponse;
import com.ims.IMS.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationAdminService {
    @Autowired
    private final AdminRepository adminRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private final JwtService jwtService;

    @Transactional
    public AuthenticationResponse register(Admin adminRegister) {
        if (adminRegister.getRole() == Role.ADMIN) {
            if (isUserExistsByUsername(adminRegister.getUsername()) || isUserExistsByPhoneNumber(adminRegister.getEmail())) {
                throw new UsernameAlreadyExistsException("Email and PhoneNumber already exists");
            }
            // Username is unique, proceed with user registration
            Admin admin = Admin.builder()
                    .fullName(adminRegister.getFullName())
                    .email(adminRegister.getEmail())
                    .phoneNumber(adminRegister.getPhoneNumber())
                    .password(passwordEncoder.encode(adminRegister.getPassword()))
                    .statusActive(true)
                    .role(Role.ADMIN)
                    .build();
            adminRepository.save(admin);
            var jwtToken = jwtService.generateTokenAdmin(admin);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } else {
            throw new LoginWrongRole("Login with wrong role");
        }
    }

    public AuthenticationResponse authenticateByEmailAndPassword(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            // User with the provided email and password exists
            var jwtToken = jwtService.generateTokenAdmin(admin);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } else {
            // Authentication failed, return null or handle as needed
            throw new WrongInformation("Information is not existence, please register");
        }
    }

    private boolean isUserExistsByUsername(String email) {
        Admin existingUser = adminRepository.findByEmail(email);
        return existingUser != null;
    }

    private boolean isUserExistsByPhoneNumber(String phonenumber) {
        Admin existingUser = adminRepository.findByPhoneNumber(phonenumber);
        return existingUser != null;
    }

}

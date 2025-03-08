package com.ims.IMS.auth.all;

import com.ims.IMS.auth.admin.AuthenticationAdminService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.common.Role;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.exception.WrongInformation;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.groupuser.AllRegister;
import com.ims.IMS.model.groupuser.AuthenticationRequest;
import com.ims.IMS.model.groupuser.AuthenticationResponse;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationAllController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationAdminService authenticationAdminService;
    @Autowired
    private AuthenticationCustomerService authenticationCustomerService;
    @Autowired
    private AuthenticationAllService authenticationAllService;
    @Autowired
    private AdminService adminService;
    @PostMapping("/login")
    public ResponseApi<?> login(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse response;
            String role = authenticationAllService.determineRoleandEmail(request.getEmail(), request.getRole());
            if (role != null) {
                response = authenticationAllService.authenticateByEmailAndPassword(request.getEmail(), request.getPassword(), role);
                return ResponseApi.success(response);
            } else {
                // Handle invalid email
                return ResponseApi.error(String.valueOf(HttpStatus.BAD_REQUEST), "{\"result\": \"Invalid email\"}");
            }
        } catch (WrongInformation wrongInformation) {
            return ResponseApi.error(String.valueOf(HttpStatus.BAD_REQUEST), "{\"result\": \"Information is not existence, please register\"}");
        }
    }

    @PostMapping("/register")
    public ResponseApi<?> register (
            @RequestBody AllRegister allRegister
    ) {
        try {
            Role role = authenticationAllService.determineByRole(allRegister.getRole());

            switch (role) {
                case ADMIN:
                    Admin adminRegister = Admin.fromAllRegister(allRegister);
                    AuthenticationResponse adminResponse = authenticationAdminService.register(adminRegister);
                    return ResponseApi.success(adminResponse);
                case CUSTOMER:
                    Customer customerRegister = Customer.fromAllRegister(allRegister);
                    AuthenticationResponse customerResponse = authenticationCustomerService.register(customerRegister);
                    return ResponseApi.success(customerResponse);
                default:
                    return ResponseApi.error(String.valueOf(HttpStatus.BAD_REQUEST), "{\"result\": \"No Role Matching\"}");
            }

        } catch (WrongInformation wrongInformation) {
            return ResponseApi.error(String.valueOf(HttpStatus.BAD_REQUEST), "{\"result\": \"Error\"}");
        } catch (UsernameAlreadyExistsException e) {
            return ResponseApi.error(String.valueOf(HttpStatus.BAD_REQUEST),"{\"result\": \"Email and Phone Number already exists\"}" );
        } catch (LoginWrongRole loginWrongRole) {
            return ResponseApi.error(String.valueOf(HttpStatus.BAD_REQUEST), "{\"result\": \"Login Wrong Role\"}");
        }
    }
}

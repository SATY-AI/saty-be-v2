package com.ims.IMS.auth.admin;


import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.exception.WrongInformation;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.lib.api.ResponseStatus;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.groupuser.AuthenticationRequest;
import com.ims.IMS.model.groupuser.AuthenticationResponse;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.CustomerRepository;
import com.ims.IMS.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ims/admin")
@RequiredArgsConstructor
public class AuthenticationAdminController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private final AuthenticationAdminService authenticationAdminService;
    @Autowired
    @Lazy
    private AdminService adminService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private CustomerRepository customerRepository;
    @PostMapping("/register")
    public ResponseApi<?> register(
            @RequestBody Admin adminRegister
    ) {
        try {
            AuthenticationResponse response = authenticationAdminService.register(adminRegister);
            return ResponseApi.success(response);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus errorStatus = new ResponseStatus("400", "Email and Phone Number already exists");
            return ResponseApi.success(errorStatus, null, null);
        } catch (LoginWrongRole loginWrongRole) {
            ResponseStatus errorStatus = new ResponseStatus("400", "Login Wrong Role");
            return ResponseApi.success(errorStatus, null, null);
        }
    }

    @PostMapping("/login")
    public ResponseApi<?> login(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse response = authenticationAdminService.authenticateByEmailAndPassword(request.getEmail(), request.getPassword());
            return ResponseApi.success(response);
        } catch (WrongInformation wrongInformation) {
            ResponseStatus errorStatus = new ResponseStatus("400", "Information is not existence, please register");
            return ResponseApi.success(errorStatus, null, null);
        }
    }
}

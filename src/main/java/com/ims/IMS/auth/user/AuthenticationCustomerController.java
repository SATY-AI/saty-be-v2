package com.ims.IMS.auth.user;


import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.LoginWrongRole;
import com.ims.IMS.exception.NotExistenceData;
import com.ims.IMS.exception.StatusNotActive;
import com.ims.IMS.exception.UsernameAlreadyExistsException;
import com.ims.IMS.exception.WrongInformation;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.lib.api.ResponseStatus;
import com.ims.IMS.model.groupuser.AuthenticationRequest;
import com.ims.IMS.model.groupuser.AuthenticationResponse;
import com.ims.IMS.model.groupuser.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ims/customer")
@RequiredArgsConstructor
public class AuthenticationCustomerController {
    @Autowired
    private final AuthenticationCustomerService authenticationCustomerService;

    @Autowired
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseApi<?> register(
            @RequestBody Customer customerRegister
    ) {
        try {
            AuthenticationResponse response = authenticationCustomerService.register(customerRegister);
            return ResponseApi.success(response);
        } catch (UsernameAlreadyExistsException e) {
            ResponseStatus errorStatus = new ResponseStatus("400", "Email and Phone Number already exists");
            return ResponseApi.success(errorStatus, null, null);
        } catch (LoginWrongRole loginWrongRole) {
            ResponseStatus errorStatus = new ResponseStatus("401", "Login Wrong Role");
            return ResponseApi.success(errorStatus, null, null);
        }
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationCustomerService.authenticate(request));
    }
    @PostMapping("/login")
    public ResponseApi<?> login(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse response = authenticationCustomerService.authenticateByEmailAndPassword(request.getEmail(), request.getPassword());
            return ResponseApi.success(response);
        } catch (WrongInformation wrongInformation) {
            ResponseStatus errorStatus = new ResponseStatus("401", "Information is not correct, please check your email and password");
            return ResponseApi.success(errorStatus, null, null);
        } catch (StatusNotActive statusNotActive) {
            ResponseStatus errorStatus = new ResponseStatus("255", "Status Not Active");
            return ResponseApi.success(errorStatus, null, null);
        } catch (NotExistenceData notExistenceData) {
            ResponseStatus errorStatus = new ResponseStatus("400", "Information is not existence, please register");
            return ResponseApi.success(errorStatus);
        }
    }
}

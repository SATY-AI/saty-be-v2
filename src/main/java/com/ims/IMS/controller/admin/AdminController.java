package com.ims.IMS.controller.admin;

import com.ims.IMS.auth.admin.AuthenticationAdminService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.CustomerRepository;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/ims/admin")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationAdminService authenticationAdminService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    private Admin validateTokenAndGetAdmin(String token) {
        String jwtToken = jwtService.extractToken(token);
        Admin admin = jwtService.decodeTokenAdmin(jwtToken);
        Admin adminData = adminRepository.findByEmailAndRole(admin.getEmail(), admin.getRole());
        if (adminData != null && adminService.checkUserRole(String.valueOf(admin.getRole()))) {
            return adminData;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }


}

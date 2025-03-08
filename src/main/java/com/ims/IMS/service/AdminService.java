package com.ims.IMS.service;
import com.ims.IMS.auth.admin.AuthenticationAdminController;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AdminService adminService;

    private final AuthenticationAdminController authenticationAdminController;

    @Autowired
    public AdminService(AuthenticationAdminController authenticationAdminController) {
        this.authenticationAdminController = authenticationAdminController;
    }

    public Admin validateTokenAndGetAdmin(String token) {
        // Extract the JWT token from the Authorization header
        String jwtToken = jwtService.extractToken(token);
        // Decode the token to get the Admin details
        Admin admin = jwtService.decodeTokenAdmin(jwtToken);
        if (admin == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        // Fetch the admin data from the repository
        Admin adminData = adminRepository.findByEmailAndRole(admin.getEmail(), admin.getRole());
        // Check if adminData is not null and has a valid role
        if (adminData == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin not found or invalid role");
        }
        // Ensure the admin has a valid role
        if (!adminService.checkUserRole(String.valueOf(admin.getRole()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized role");
        }
        // Return the validated admin data
        return adminData;
    }


    public boolean checkUserRole (String role) {
        if (role.equals("ADMIN")) {
            return true;
        } else {
            return false;
        }
    }

}

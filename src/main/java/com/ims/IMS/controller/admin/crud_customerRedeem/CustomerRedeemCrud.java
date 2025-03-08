package com.ims.IMS.controller.admin.crud_customerRedeem;

import com.ims.IMS.api.customerRedeem.CustomerRedeemRequest;
import com.ims.IMS.api.customerRedeem.CustomerRedeemResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.mapper.SatyRiceProductMapping;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.DeviceService;
import com.ims.IMS.service.customerRedeemService.CustomerRedeemService;
import com.ims.IMS.service.rice.RiceProductService;
import com.ims.IMS.service.warehouse.WareHouseDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ims/admin")
@RequiredArgsConstructor
public class CustomerRedeemCrud {
    private final WareHouseDataService wareHouseDataService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationAllService authenticationAllService;
    @Autowired
    private AuthenticationCustomerService authenticationCustomerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RiceProductService riceProductService;
    @Autowired
    private SatyRiceProductMapping satyRiceProductMapping;
    private final CustomerRedeemService customerRedeemService;

    // Get all CustomerRedeems
    @GetMapping("/list-customer-redeem")
    public ResponseApi<List<CustomerRedeemResponse>> getAllCustomerRedeems(
            @RequestHeader("Authorization") String token
    ) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        return ResponseApi.success(customerRedeemService.getAllCustomerRedeems());
    }

    // Get a single CustomerRedeem by ID
    @GetMapping("/{id}")
    public ResponseApi<CustomerRedeemResponse> getCustomerRedeemById(@PathVariable Integer id) {
        Optional<CustomerRedeemResponse> response = customerRedeemService.getCustomerRedeemById(id);
        return response.map(ResponseApi::success).orElseGet(() -> ResponseApi.error("404", "CustomerRedeem not found"));
    }

    // Create a new CustomerRedeem
    @PostMapping
    public ResponseApi<CustomerRedeemResponse> createCustomerRedeem(@RequestBody CustomerRedeemRequest request) {
        return ResponseApi.success(customerRedeemService.createCustomerRedeem(request));
    }

    // Update an existing CustomerRedeem
    @PutMapping("/{id}")
    public ResponseApi<CustomerRedeemResponse> updateCustomerRedeem(@PathVariable Integer id, @RequestBody CustomerRedeemRequest request) {
        Optional<CustomerRedeemResponse> response = customerRedeemService.updateCustomerRedeem(id, request);
        return response.map(ResponseApi::success).orElseGet(() -> ResponseApi.error("404", "CustomerRedeem not found"));
    }

    // Delete a CustomerRedeem
    @DeleteMapping("/delete-customer-redeem/{id}")
    public ResponseApi<String> deleteCustomerRedeem(@PathVariable Integer id) {
        boolean deleted = customerRedeemService.deleteCustomerRedeem(id);
        return deleted ? ResponseApi.success("CustomerRedeem with ID: " + id + " deleted successfully") :
                ResponseApi.error("404", "CustomerRedeem not found");
    }


}

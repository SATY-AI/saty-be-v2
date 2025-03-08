package com.ims.IMS.controller.admin.crud_warehouse;

import com.ims.IMS.api.warehouse.*;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.exception.*;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.mapper.SatyRiceProductMapping;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.DeviceService;
import com.ims.IMS.service.rice.RiceProductService;
import com.ims.IMS.service.warehouse.WareHouseDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.IMS.api.warehouse.WareHouseDataResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/ims/admin")
@RequiredArgsConstructor
public class WarehouseCrud {
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

    @PostMapping("/add-item-warehouse")
    public ResponseApi<WareHouseDataResponse> createWarehouse(
            @RequestHeader("Authorization") String token,
            @RequestBody WareHouseDataRegisterRequest request) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
        try {
            WareHouseDataResponse response = wareHouseDataService.createWarehouse(request);
            return ResponseApi.success(response);
        } catch (WarehouseValidationException ex) {
            // Handle the exception and return an error response
            return ResponseApi.error("400", ex.getMessage()); // Customize this based on your ResponseApi implementation
        } catch (WarehouseProcessingException ex) {
            return ResponseApi.error("401", ex.getMessage());
        } catch (CodeProductValidationException ex) {
            return ResponseApi.error("402", ex.getMessage());
        } catch (RiceProductValidationException ex) {
            return ResponseApi.error("403", ex.getMessage());
        }
    }

    @PostMapping("/add-detail-warehouse-by-filtering")
    public ResponseApi<WareHouseDetailResponse> getDetailWarehouse(
            @RequestHeader("Authorization") String token,
            @RequestBody WareHouseDetailRequest request,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageOffset) {
        // Validate admin authorization
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        try {
            // Fetch warehouse details
            WareHouseDetailResponse response = wareHouseDataService.detailWareHouse(request);
            return ResponseApi.success(response);
        } catch (WarehouseNotFoundException e) {
            return ResponseApi.error("404", e.getMessage()); // More specific error handling
        } catch (WarehouseValidationException e) {
            return ResponseApi.error("400", e.getMessage()); // More specific error handling
        } catch (Exception e) {
            return ResponseApi.error("500", "Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/list-item-warehouse")
    public ResponseApi<List<WareHouseDataResponse>> getAllWarehouses(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
        List<WareHouseDataResponse> response = wareHouseDataService.getAllWarehouses(page, size);
        return ResponseApi.success(response);
    }

    @PutMapping("/update-item-warehouse")
    public ResponseApi<WareHouseDataUpdateResponse> updateWarehouse(
            @RequestHeader("Authorization") String token,
            @RequestBody WareHouseDataUpdateRequest request) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
        WareHouseDataUpdateResponse response = wareHouseDataService.updateWarehouse(request.uniqueCodeWareHouse(), request);
        return ResponseApi.success(response);
    }

    @DeleteMapping("/delete-item-warehouse/{uniqueCodeWarehouse}")
    public ResponseApi<String> deleteWarehouse( @RequestHeader("Authorization") String token,
                                                @PathVariable String uniqueCodeWarehouse) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
        wareHouseDataService.deleteWarehouse(uniqueCodeWarehouse);
        return ResponseApi.success("Warehouse deleted successfully.");
    }
}

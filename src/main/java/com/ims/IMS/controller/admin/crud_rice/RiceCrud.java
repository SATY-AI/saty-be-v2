package com.ims.IMS.controller.admin.crud_rice;

import com.ims.IMS.api.rice.*;
import com.ims.IMS.mapper.SatyRiceProductMapping;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.riceProduct.RiceProduct;
import com.ims.IMS.service.rice.RiceProductService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.ims.IMS.repository.AdminRepository;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.repository.DeviceRepository;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.DeviceService;
import com.ims.IMS.lib.api.ResponseApi;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ims/admin")
public class RiceCrud {
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

    @GetMapping("/list-rice-product")
    public ResponseApi<List<ListRiceProductResponse>> getListRiceProduct(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Validate the admin token
        Admin adminData = adminService.validateTokenAndGetAdmin(token); // Ensure the admin has valid permissions
        // Create a Pageable object
        Pageable pageable = PageRequest.of(page, size);
        // Fetch paginated rice products
        Page<RiceProduct> riceProductsPage = riceProductService.listAllRiceProducts(pageable);
        // Map the list of RiceProduct entities to ListRiceProductResponse
        List<ListRiceProductResponse> responseList = riceProductsPage.stream()
                .map(satyRiceProductMapping::toListRiceProductResponse)
                .collect(Collectors.toList());
        // Return the response wrapped in ResponseApi
        return ResponseApi.success(responseList);
    }

    @GetMapping("/list-rice-product-unique-code")
    public ResponseApi<List<ListRiceProductUniqueCodeResponse>> getListRiceProductUniqueCodeResponse(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Validate the admin token
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Create a Pageable object
        Pageable pageable = PageRequest.of(page, size);
        // Fetch paginated rice products
        Page<RiceProduct> riceProductsPage = riceProductService.listAllRiceProducts(pageable);
        // Map the list of RiceProduct entities to ListRiceProductUniqueCodeResponse
        List<ListRiceProductUniqueCodeResponse> responseList = riceProductsPage.stream()
                .map(riceProduct -> new ListRiceProductUniqueCodeResponse(riceProduct.getUniqueCode()))
                .collect(Collectors.toList());
        // Return the response wrapped in ResponseApi
        return ResponseApi.success(responseList);
    }

    // **2. Add a New Rice Product**
    @PostMapping("/add-rice-product")
    public ResponseApi<RiceProductResponse> addRiceProduct(
            @RequestHeader("Authorization") String token,
            @RequestBody RiceProductRegisterRequest request
    ) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token); // Validate admin
        // Check if the unique code already exists
        if (riceProductService.existsByUniqueCode(request.uniqueCode())) {
            return ResponseApi.error("400","Unique code '" + request.uniqueCode() + "' already exists.");
        }
        RiceProductResponse response = riceProductService.registerRiceProduct(request);
        return ResponseApi.success(response);
    }

    // **3. Update an Existing Rice Product**
    @PutMapping("/update-rice-product")
    public ResponseApi<UpdateRiceProductResponse> updateRiceProduct(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateRiceProductRequest request
    ) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token); // Validate admin
        UpdateRiceProductResponse response = riceProductService.updateRiceProduct(request);
        return ResponseApi.success(response);
    }

    // **4. Delete a Rice Product**
    @DeleteMapping("/delete-rice-product/{uniqueCode}")
    public ResponseApi<String> deleteRiceProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable String uniqueCode
    ) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token); // Validate admin
        riceProductService.deleteRiceProduct(uniqueCode);
        return ResponseApi.success("Product deleted successfully");
    }








}

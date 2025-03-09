package com.ims.IMS.controller.admin.crud_qrcode;

import com.ims.IMS.api.qrProductInfo.Admin.ProductInfoUpdateRequest;
import com.ims.IMS.api.qrProductInfo.ProductInfoRequest;
import com.ims.IMS.api.qrProductInfo.ProductInfoResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.riceProduct.RiceProduct;
import com.ims.IMS.model.warehouse.WareHouseData;
import com.ims.IMS.repository.CustomerRedeem.CustomerRedeemRepository;
import com.ims.IMS.repository.Rice.RiceProductRepository;
import com.ims.IMS.repository.WareHouse.WareHouseDataRepository;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.redeemProductService.ProductRedeemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ims/admin")
public class QrAdminCrud {

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
    private RiceProductRepository riceProductRepository;
    @Autowired
    private WareHouseDataRepository wareHouseDataRepository;
    @Autowired
    private ProductRedeemService productRedeemService;
    @Autowired
    private CustomerRedeemRepository customerRedeemRepository;

    @PostMapping("/post-info-product")
    public ResponseApi<ProductInfoResponse> postInfoProductAdmin(
            @RequestHeader("Authorization") String token,
            @RequestBody ProductInfoRequest request) {

        // Validate the token and get the customer (You might want to replace this with your actual validation method)
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Find the product by unique code
        Optional<RiceProduct> riceProduct = riceProductRepository.findByUniqueCode(request.codeProduct());
        if (riceProduct == null) {
            return ResponseApi.error("400", "Product with the specified code does not exist.");
        }
        // Find the warehouse by unique code
        // Find all warehouse entries with the given unique code
        List<WareHouseData> wareHouseDataList = wareHouseDataRepository.findAllByUniqueCodeWareHouse(request.codeWarehouse());
        if (wareHouseDataList.isEmpty()) {
            return ResponseApi.error("400", "Warehouse with the specified code does not exist.");
        }
        WareHouseData wareHouseData = wareHouseDataList.get(0);

        // Create a response with the product and warehouse details (customize this as needed)
        ProductInfoResponse response = new ProductInfoResponse(
                //Get RiceProduct Information
                "Product and Warehouse found",
                //Get Product Information
                riceProduct.get().getUniqueCode(),
                riceProduct.get().getName(),
                riceProduct.get().getDescription(),
                riceProduct.get().getManufacturer(),
                riceProduct.get().getAdvantage(),
                riceProduct.get().getDisadvantage(),
                riceProduct.get().getOrigin(),
                riceProduct.get().getRiceType(),
                riceProduct.get().getNutritionFacts(),
                riceProduct.get().getHealthBenefits(),
                riceProduct.get().getCookingInstructions(),
                riceProduct.get().getStorageInstructions(),
                riceProduct.get().getFoodPairings(),
                riceProduct.get().getImageUrl(),
                //Get Warehouse Information
                wareHouseData.getUniqueCodeWareHouse(),
                //Get Unique Code Product
                request.codeProduct(),
                //Get others data from WareHouseData
                wareHouseData.getName(),
                wareHouseData.getPricePerKg(),
                wareHouseData.getStockQuantity(),
                wareHouseData.getInformationWareHouse(),
                wareHouseData.getAddress(),
                wareHouseData.getPhone()
        );
        return ResponseApi.success(response);
    }

    @PutMapping("/update-post-info-product")
    public ResponseApi<ProductInfoResponse> updateInfoProductAdmin(
            @RequestHeader("Authorization") String token,
            @RequestBody ProductInfoUpdateRequest request) {

        // Validate the token and get the admin user
        Admin adminData = adminService.validateTokenAndGetAdmin(token);

        // Find the product by unique code
        Optional<RiceProduct> optionalRiceProduct = riceProductRepository.findByUniqueCode(request.uniqueCodeProduct());
        if (optionalRiceProduct.isEmpty()) {
            return ResponseApi.error("400", "Product with the specified code does not exist.");
        }
        RiceProduct riceProduct = optionalRiceProduct.get();

        // Find all warehouse entries with the given unique code
        List<WareHouseData> wareHouseDataList = wareHouseDataRepository.findAllByUniqueCodeWareHouse(request.uniqueCodeWareHouse());
        if (wareHouseDataList.isEmpty()) {
            return ResponseApi.error("400", "Warehouse with the specified code does not exist.");
        }
        WareHouseData wareHouseData = wareHouseDataList.get(0);

        // Update Product Information
        riceProduct.setName(request.name());
        riceProduct.setDescription(request.description());
        riceProduct.setManufacturer(request.manufacturer());
        riceProduct.setAdvantage(request.advantage());
        riceProduct.setDisadvantage(request.disadvantage());
        riceProduct.setOrigin(request.origin());
        riceProduct.setRiceType(request.riceType());
        riceProduct.setNutritionFacts(request.nutritionFacts());
        riceProduct.setHealthBenefits(request.healthBenefits());
        riceProduct.setCookingInstructions(request.cookingInstructions());
        riceProduct.setStorageInstructions(request.storageInstructions());
        riceProduct.setFoodPairings(request.foodPairings());
        riceProduct.setImageUrl(request.imageUrl());

        // Save the updated product
        riceProductRepository.save(riceProduct);

        // Update Warehouse Information
        wareHouseData.setName(request.nameWareHouse());
        wareHouseData.setPricePerKg(request.pricePerKg());
        wareHouseData.setStockQuantity(request.stockQuantity());
        wareHouseData.setInformationWareHouse(request.informationWareHouse());
        wareHouseData.setAddress(request.address());
        wareHouseData.setPhone(request.phone());

        // Save the updated warehouse data
        wareHouseDataRepository.save(wareHouseData);

        // Prepare response
        ProductInfoResponse response = new ProductInfoResponse(
                "Product and Warehouse updated successfully",
                riceProduct.getUniqueCode(),
                riceProduct.getName(),
                riceProduct.getDescription(),
                riceProduct.getManufacturer(),
                riceProduct.getAdvantage(),
                riceProduct.getDisadvantage(),
                riceProduct.getOrigin(),
                riceProduct.getRiceType(),
                riceProduct.getNutritionFacts(),
                riceProduct.getHealthBenefits(),
                riceProduct.getCookingInstructions(),
                riceProduct.getStorageInstructions(),
                riceProduct.getFoodPairings(),
                riceProduct.getImageUrl(),
                wareHouseData.getUniqueCodeWareHouse(),
                riceProduct.getUniqueCode(),
                wareHouseData.getName(),
                wareHouseData.getPricePerKg(),
                wareHouseData.getStockQuantity(),
                wareHouseData.getInformationWareHouse(),
                wareHouseData.getAddress(),
                wareHouseData.getPhone()
        );
        return ResponseApi.success(response);
    }

}

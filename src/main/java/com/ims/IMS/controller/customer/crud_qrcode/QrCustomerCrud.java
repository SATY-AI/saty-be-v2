package com.ims.IMS.controller.customer.crud_qrcode;

import com.ims.IMS.api.qrProductInfo.CustomerRedeem.CheckProductWithoutLogin;
import com.ims.IMS.api.qrProductInfo.CustomerRedeem.CheckRedeemProductRequest;
import com.ims.IMS.api.qrProductInfo.CustomerRedeem.CheckRedeemProductResponse;
import com.ims.IMS.api.qrProductInfo.ProductInfoRequest;
import com.ims.IMS.api.qrProductInfo.ProductInfoResponse;
import com.ims.IMS.auth.all.AuthenticationAllService;
import com.ims.IMS.auth.user.AuthenticationCustomerService;
import com.ims.IMS.config.JwtService;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.model.CustomerRedeem.CustomerRedeem;
import com.ims.IMS.model.groupuser.Customer;
import com.ims.IMS.model.riceProduct.RiceProduct;
import com.ims.IMS.model.warehouse.WareHouseData;
import com.ims.IMS.repository.CustomerRedeem.CustomerRedeemRepository;
import com.ims.IMS.repository.Rice.RiceProductRepository;
import com.ims.IMS.repository.WareHouse.WareHouseDataRepository;
import com.ims.IMS.service.CustomerService;
import com.ims.IMS.service.redeemProductService.ProductRedeemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ims/customer")
public class QrCustomerCrud {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationAllService authenticationAllService;
    @Autowired
    private AuthenticationCustomerService authenticationCustomerService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RiceProductRepository riceProductRepository;
    @Autowired
    private WareHouseDataRepository wareHouseDataRepository;
    @Autowired
    private ProductRedeemService productRedeemService;
    @Autowired
    private CustomerRedeemRepository customerRedeemRepository;

    @PostMapping("/post-info-product")
    public ResponseApi<ProductInfoResponse> postInfoProduct(
            @RequestHeader("Authorization") String token,
            @RequestBody ProductInfoRequest request) {

        // Validate the token and get the customer (You might want to replace this with your actual validation method)
        Customer customerData = customerService.validateTokenAndGetCustomer(token);
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

    @PostMapping("/check-redeem-product")
    public ResponseApi<?> checkRedeemProduct(
            @RequestBody CheckRedeemProductRequest checkRedeemProductRequest) {
        // Validate request data
        if (checkRedeemProductRequest.codeProduct() == null || checkRedeemProductRequest.codeProduct().isEmpty()) {
            return ResponseApi.error("400", "Product code must not be empty.");
        }
        if (checkRedeemProductRequest.codeWarehouse() == null || checkRedeemProductRequest.codeWarehouse().isEmpty()) {
            return ResponseApi.error("400", "Warehouse code must not be empty.");
        }
        // Find the product by unique code
        Optional<RiceProduct> riceProduct = riceProductRepository.findByUniqueCode(checkRedeemProductRequest.codeProduct());
        if (riceProduct.isEmpty()) {
            return ResponseApi.error("400", "Product with the specified code does not exist.");
        }
        // Find all warehouse entries with the given unique code
        List<WareHouseData> wareHouseDataList = wareHouseDataRepository.findAllByUniqueCodeWareHouse(checkRedeemProductRequest.codeWarehouse());
        if (wareHouseDataList.isEmpty()) {
            return ResponseApi.error("400", "Warehouse with the specified code does not exist.");
        }
        // Select warehouse (if multiple exist, you may need additional selection logic)
        WareHouseData wareHouseData = wareHouseDataList.get(0);
        if (checkRedeemProductRequest.codeCustomer().equals("CUSTOMER_TYPE_NOT_LOGIN")) {
            RiceProduct riceProductData = riceProduct.get();
            CheckProductWithoutLogin checkProductWithoutLogin = new CheckProductWithoutLogin(
                    "Product without Login",
                    riceProductData.getUniqueCode(),
                    riceProductData.getName(),
                    riceProductData.getDescription(),
                    riceProductData.getManufacturer(),
                    riceProductData.getAdvantage(),
                    riceProductData.getDisadvantage(),
                    riceProductData.getOrigin(),
                    riceProductData.getRiceType(),
                    riceProductData.getNutritionFacts(),
                    riceProductData.getHealthBenefits(),
                    riceProductData.getCookingInstructions(),
                    riceProductData.getStorageInstructions(),
                    riceProductData.getFoodPairings(),
                    riceProductData.getImageUrl(),
                    // Get Warehouse Information
                    wareHouseData.getUniqueCodeWareHouse(),
                    // Get from UniqueCode Product
                    checkRedeemProductRequest.codeProduct(),
                    // Other Data
                    wareHouseData.getName(),
                    wareHouseData.getPricePerKg(),
                    wareHouseData.getStockQuantity(),
                    wareHouseData.getInformationWareHouse(),
                    wareHouseData.getAddress(),
                    wareHouseData.getPhone()
            );
            return ResponseApi.success(checkProductWithoutLogin);
        } else if (checkRedeemProductRequest.codeCustomer().equals("CUSTOMER_TYPE_LOGIN")) {
            // Validate user existence
            boolean userExists = productRedeemService.checkUserExistence(
                    checkRedeemProductRequest.username(),
                    checkRedeemProductRequest.password()
            );
            if (!userExists) {
                return ResponseApi.error("404", "Wrong UserName and Password");
            } else {
                // Extract product details
                RiceProduct riceProductData = riceProduct.get();

                CustomerRedeem customerRedeem = CustomerRedeem.builder()
                                .username(checkRedeemProductRequest.username())
                                        .password(checkRedeemProductRequest.password())
                                                .uniqueCodeProduct(riceProductData.getUniqueCode())
                                                        .uniqueCodeWareHouse((wareHouseData.getUniqueCodeWareHouse()))
                                                                .timeVerified(ZonedDateTime.now())
                                                                        .messageTrungMua("Message Trung Mua Demo")
                                                                                .build();
                try {
                    // Save CustomerRedeem entity
                    customerRedeemRepository.save(customerRedeem);
                } catch (Exception e) {
                    return ResponseApi.error("500", "Failed to save customer redeem data.");
                }
                // Build response
                CheckRedeemProductResponse checkRedeemProductResponse = new CheckRedeemProductResponse(
                        "Product Redeem Response",
                        "Message ",
                        "Message Trung Mua Demo",
                        riceProductData.getUniqueCode(),
                        riceProductData.getName(),
                        riceProductData.getDescription(),
                        riceProductData.getManufacturer(),
                        riceProductData.getAdvantage(),
                        riceProductData.getDisadvantage(),
                        riceProductData.getOrigin(),
                        riceProductData.getRiceType(),
                        riceProductData.getNutritionFacts(),
                        riceProductData.getHealthBenefits(),
                        riceProductData.getCookingInstructions(),
                        riceProductData.getStorageInstructions(),
                        riceProductData.getFoodPairings(),
                        riceProductData.getImageUrl(),
                        // Get Warehouse Information
                        wareHouseData.getUniqueCodeWareHouse(),
                        // Get from UniqueCode Product
                        checkRedeemProductRequest.codeProduct(),
                        // Other Data
                        wareHouseData.getName(),
                        wareHouseData.getPricePerKg(),
                        wareHouseData.getStockQuantity(),
                        wareHouseData.getInformationWareHouse(),
                        wareHouseData.getAddress(),
                        wareHouseData.getPhone()
                );
                return ResponseApi.success(checkRedeemProductResponse);
            }

        }
        //If neither CUSTOMER_TYPE_NOT_LOGIN nor CUSTOMER_TYPE_LOGIN is provided
        return ResponseApi.error("400", "Invalid Customer Type.");
    }

}

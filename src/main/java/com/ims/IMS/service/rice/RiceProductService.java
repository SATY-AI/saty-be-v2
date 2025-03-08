package com.ims.IMS.service.rice;

import com.ims.IMS.api.rice.RiceProductRegisterRequest;
import com.ims.IMS.api.rice.RiceProductResponse;
import com.ims.IMS.api.rice.UpdateRiceProductRequest;
import com.ims.IMS.api.rice.UpdateRiceProductResponse;
import com.ims.IMS.exception.UniqueCodeExistsException;
import com.ims.IMS.mapper.SatyRiceProductMapping;
import com.ims.IMS.model.riceProduct.RiceProduct;
import com.ims.IMS.repository.Rice.RiceProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class RiceProductService {
    @Autowired
    private RiceProductRepository riceProductRepository;
    @Autowired
    private SatyRiceProductMapping satyRiceProductMapping;

    @Autowired
    public RiceProductService(RiceProductRepository riceProductRepository, SatyRiceProductMapping mapper) {
        this.riceProductRepository = riceProductRepository;
        this.satyRiceProductMapping = mapper;
    }

    // Method to fetch all rice products from the repository
    public Page<RiceProduct> listAllRiceProducts(Pageable pageable) {
        return riceProductRepository.findAll(pageable);
    }

    public boolean existsByUniqueCode(String uniqueCode) {
        return riceProductRepository.existsByUniqueCode(uniqueCode);
    }
    // Method to register a new rice product
    public RiceProductResponse registerRiceProduct(RiceProductRegisterRequest request) {
        // Check if the unique code already exists
        if (riceProductRepository.existsByUniqueCode(request.uniqueCode())) {
            throw new UniqueCodeExistsException("Unique code '" + request.uniqueCode() + "' already exists.");
        }
        // Map request to entity and save it
        RiceProduct riceProduct = satyRiceProductMapping.toRiceProduct(request);
        riceProduct = riceProductRepository.save(riceProduct);
        return satyRiceProductMapping.toRiceProductResponse(riceProduct, "Product registered successfully");
    }

    // Method to update an existing rice product
    public UpdateRiceProductResponse updateRiceProduct(UpdateRiceProductRequest request) {
        RiceProduct existingProduct = riceProductRepository.findByUniqueCode(request.uniqueCode())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        satyRiceProductMapping.updateRiceProductFromRequest(request, existingProduct);
        riceProductRepository.save(existingProduct);
        return satyRiceProductMapping.toUpdateRiceProductResponse(existingProduct, "Product updated successfully");
    }

    public void deleteRiceProduct(String uniqueCode) {
        RiceProduct existingProduct = riceProductRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        riceProductRepository.delete(existingProduct);
    }

}

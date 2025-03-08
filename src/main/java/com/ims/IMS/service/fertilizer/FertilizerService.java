package com.ims.IMS.service.fertilizer;


import com.ims.IMS.api.fertilizer.request.AddFertilizerRequest;
import com.ims.IMS.api.fertilizer.response.GetAllFertilizerResponse;
import com.ims.IMS.api.fertilizer.request.UpdateFertilizerRequest;
import com.ims.IMS.mapper.ProductAgriMapping;
import com.ims.IMS.model.fertilizerProduct.Fertilizer;
import com.ims.IMS.repository.Fertilizer.FertilizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FertilizerService {

    @Autowired
    private FertilizerRepository fertilizerRepository;

    @Autowired
    private ProductAgriMapping productAgriMapping;

    // Get all fertilizers using the mapping
    public List<GetAllFertilizerResponse> getAllFertilizers() {
        return productAgriMapping.toGetAllFertilizerResponseList(fertilizerRepository.findAll());
    }

    public Optional<GetAllFertilizerResponse> getFertilizerById(Long id) {
        return fertilizerRepository.findById(id)
                .map(productAgriMapping::toGetAllFertilizerResponse); // Map Fertilizer entity to DTO
    }

    // New method to find Fertilizer by unique code
    public Optional<Fertilizer> findByUniqueCode(String uniqueCode) {
        return fertilizerRepository.findByUniqueCode(uniqueCode);
    }

    // Add a new fertilizer
    public Fertilizer addFertilizer(AddFertilizerRequest request) {
        Fertilizer fertilizer = new Fertilizer();
        fertilizer.setName(request.name());
        fertilizer.setPricePerKg(request.pricePerKg());
        fertilizer.setStockQuantity(request.stockQuantity());
        fertilizer.setDescription(request.description());
        fertilizer.setManufacturer(request.manufacturer());
        return fertilizerRepository.save(fertilizer);
    }

    // Update an existing fertilizer
    public Fertilizer updateFertilizer(Long id, UpdateFertilizerRequest fertilizerDetails) {
        // Find existing fertilizer by ID, throw exception if not found
        Fertilizer fertilizer = fertilizerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found"));
        // Update the existing fertilizer with new details
        fertilizer.setName(fertilizerDetails.name());
        fertilizer.setPricePerKg(fertilizerDetails.pricePerKg());
        fertilizer.setStockQuantity(fertilizerDetails.stockQuantity());
        fertilizer.setDescription(fertilizerDetails.description());
        fertilizer.setManufacturer(fertilizerDetails.manufacturer());
        // Save and return updated fertilizer
        return fertilizerRepository.save(fertilizer);
    }

    public boolean deleteFertilizerById(Long id) {
        Optional<Fertilizer> fertilizer = fertilizerRepository.findById(id);
        if (fertilizer.isPresent()) {
            fertilizerRepository.delete(fertilizer.get());
            return true;
        } else {
            return false;
        }
    }

    // Delete a fertilizer
    public void deleteFertilizer(Long id) {
        fertilizerRepository.deleteById(id);
    }
}

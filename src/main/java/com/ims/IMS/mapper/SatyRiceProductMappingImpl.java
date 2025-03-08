package com.ims.IMS.mapper;

import com.ims.IMS.api.rice.*;
import com.ims.IMS.model.riceProduct.RiceProduct;
import org.springframework.stereotype.Component;

@Component
public class SatyRiceProductMappingImpl implements SatyRiceProductMapping{
    @Override
    public ListRiceProductResponse toListRiceProductResponse(RiceProduct riceProduct) {
        // Map RiceProduct to ListRiceProductResponse
        return new ListRiceProductResponse(
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
                riceProduct.getImageUrl()
        );
    }

    @Override
    public RiceProductResponse toRiceProductResponse(RiceProduct riceProduct, String message) {
        // Map RiceProduct to RiceProductResponse with a message
        return new RiceProductResponse(
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
                message
        );
    }

    @Override
    public UpdateRiceProductResponse toUpdateRiceProductResponse(RiceProduct riceProduct, String message) {
        // Map RiceProduct to UpdateRiceProductResponse with a message
        return new UpdateRiceProductResponse(
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
                message
        );
    }

    @Override
    public RiceProduct toRiceProduct(RiceProductRegisterRequest request) {
        // Map RiceProductRegisterRequest to RiceProduct entity
        return RiceProduct.builder()
                .uniqueCode(request.uniqueCode())
                .name(request.name())
                .description(request.description())
                .manufacturer(request.manufacturer())
                .advantage(request.advantage())
                .disadvantage(request.disadvantage())
                .origin(request.origin())
                .riceType(request.riceType())
                .nutritionFacts(request.nutritionFacts())
                .healthBenefits(request.healthBenefits())
                .cookingInstructions(request.cookingInstructions())
                .storageInstructions(request.storageInstructions())
                .foodPairings(request.foodPairings())
                .imageUrl(request.imageUrl())
                .build();
    }

    @Override
    public void updateRiceProductFromRequest(UpdateRiceProductRequest request, RiceProduct riceProduct) {
        // Update the existing RiceProduct entity from UpdateRiceProductRequest
        riceProduct.setUniqueCode(request.uniqueCode());
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
    }
}

package com.ims.IMS.api.rice;

public record ListRiceProductResponse(
        String uniqueCode,
        String name,
        String description,
        String manufacturer,
        String advantage,
        String disadvantage,
        String origin,
        String riceType,
        String nutritionFacts,
        String healthBenefits,
        String cookingInstructions,
        String storageInstructions,
        String foodPairings,
        String imageUrl
) {}

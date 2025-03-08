package com.ims.IMS.api.fertilizer.response;

public record GetAllFertilizerResponse(
        Integer id,
        String name,
        String uniqueCode,
        double pricePerKg,
        int stockQuantity,
        String description,
        String manufacturer
) {}
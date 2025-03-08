package com.ims.IMS.api.fertilizer.request;

public record UpdateFertilizerRequest(
        Integer id,
        String name,
        String uniqueCode,
        double pricePerKg,
        int stockQuantity,
        String description,
        String manufacturer
) {}

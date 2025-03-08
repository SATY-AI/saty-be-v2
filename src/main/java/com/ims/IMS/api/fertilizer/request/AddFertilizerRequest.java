package com.ims.IMS.api.fertilizer.request;

public record AddFertilizerRequest(
        String name,
        String uniqueCode,
        double pricePerKg,
        int stockQuantity,
        String description,
        String manufacturer
) {}

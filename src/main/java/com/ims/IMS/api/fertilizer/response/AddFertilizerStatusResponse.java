package com.ims.IMS.api.fertilizer.response;

import com.ims.IMS.api.fertilizer.request.AddFertilizerRequest;

public record AddFertilizerStatusResponse(
        String status,
        String message, // Detailed message
        long fertilizerId, // ID of the added fertilizer
        AddFertilizerRequest addFertilizerRequest // Original request
) {}

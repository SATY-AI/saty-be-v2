package com.ims.IMS.api.fertilizer.request;

import com.ims.IMS.api.progress.request.ProgressPlantRequest;
import com.ims.IMS.api.satyjourney.data.SatyJourneyDataRequest;

public record FertilizerUsageLogRequest(
        SatyJourneyDataRequest satyJourneyData,
        AddFertilizerRequest addFertilizerRequest,
        double amountUsed,
        String dateUsed,
        ProgressPlantRequest progressPlantRequest
) {}

package com.ims.IMS.api.fertilizer.response;

import com.ims.IMS.api.fertilizer.request.AddFertilizerRequest;
import com.ims.IMS.api.satyjourney.data.SatyJourneyDataRequest;

public record FertilizerUsageLogResponse(
        SatyJourneyDataRequest satyJourneyData,
        double amountUsed,
        String dateUsed
) {}

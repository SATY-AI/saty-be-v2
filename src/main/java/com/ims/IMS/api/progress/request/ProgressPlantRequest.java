package com.ims.IMS.api.progress.request;

import com.ims.IMS.model.SatyJourney.SatyDevice;
import java.time.LocalDateTime;

public record ProgressPlantRequest(
        String plantName,
        String plantType,
        double height,
        LocalDateTime plantingDate,
        String growthStage,
        boolean isPerennial,
        SatyDevice device // Assuming SatyDevice is passed here
) {}
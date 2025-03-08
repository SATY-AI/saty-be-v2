package com.ims.IMS.mapper;

import com.ims.IMS.api.satyjourney.data.SatyJourneyDataRequest;
import com.ims.IMS.api.satyjourney.device.SatyDeviceInformationRequest;
import com.ims.IMS.model.SatyJourney.SatyDevice;
import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import org.springframework.stereotype.Component;

@Component
public interface SatyJourneyDataMapper {
    // Method to map SatyJourneyData entity to SatyJourneyDataRequest DTO
    SatyJourneyDataRequest toSatyJourneyDataRequest(SatyJourneyData satyJourneyData);
    // Method to map SatyJourneyDataRequest DTO to SatyJourneyData entity
    SatyJourneyData toSatyJourneyData(SatyJourneyDataRequest satyJourneyDataRequest);
    // Map request DTO to SatyDevice entity
    SatyDevice toEntity(SatyDeviceInformationRequest request);
    // Map SatyDevice entity to request DTO if needed
    SatyDeviceInformationRequest toRequest(SatyDevice entity);
}

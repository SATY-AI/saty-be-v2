package com.ims.IMS.mapper;

import com.ims.IMS.api.CustomerImageDetailResponse;
import com.ims.IMS.api.InsectListDataResponse;
import com.ims.IMS.api.satyjourney.data.SatyDataRequest;
import com.ims.IMS.api.satyjourney.data.SatyDataResponse;
import com.ims.IMS.api.device.ListDeviceResponse;
import com.ims.IMS.api.image.ListImageResponse;
import com.ims.IMS.api.location.ListLocationResponse;
import com.ims.IMS.api.location.LocationIDResponse;
import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import com.ims.IMS.model.insect.InsectListData;
import com.ims.IMS.model.imsprocessing.Device;
import com.ims.IMS.model.imsprocessing.Image;
import com.ims.IMS.model.imsprocessing.ImageDetail;
import com.ims.IMS.model.imsprocessing.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", implementationName = "CoreIMSMapper")
public interface ImsMapping {
    ListDeviceResponse mapFromDevice(Device device, String secretKey, String email, String phoneNumber);
    LocationIDResponse mapToLocationID(Location location);
    ListLocationResponse mapFromLocation(Location location);
    CustomerImageDetailResponse mapToCustomerImageDetailResponse(ImageDetail imageDetail);
    ListImageResponse mapFromImage(Image image);
    List<InsectListDataResponse> toResponseList(List<InsectListData> insectListDataList);
    SatyJourneyData mapToSatyJourneyData(SatyDataRequest satyDataRequest);
    SatyDataResponse mapToSatyDataResponse(SatyJourneyData deoCaData);
}

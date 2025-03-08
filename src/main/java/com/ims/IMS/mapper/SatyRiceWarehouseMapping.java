package com.ims.IMS.mapper;

import com.ims.IMS.api.warehouse.*;
import com.ims.IMS.model.warehouse.WareHouseData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", implementationName = "CoreWareHouseDataMapper")
public interface SatyRiceWarehouseMapping {
    // Map entity to response DTO
    WareHouseDataResponse toWareHouseDataResponse(WareHouseData wareHouseData);
    // Map entity to update response DTO
    WareHouseDataUpdateResponse toWareHouseDataUpdateResponse(WareHouseData wareHouseData, String message);
    // Map register request to entity
    WareHouseData toWareHouseData(WareHouseDataRegisterRequest request);

    WareHouseDetailResponse toWareHouseDetailResponse(WareHouseData wareHouseData);

    // Map update request to existing entity
    void updateWareHouseDataFromRequest(WareHouseDataUpdateRequest request, @MappingTarget WareHouseData wareHouseData);

}

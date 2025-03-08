package com.ims.IMS.mapper;


import com.ims.IMS.api.rice.*;
import com.ims.IMS.model.riceProduct.RiceProduct;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", implementationName = "CoreDiaryMapper")
public interface SatyRiceProductMapping {

    // Map from RiceProduct entity to ListRiceProductResponse
    ListRiceProductResponse toListRiceProductResponse(RiceProduct riceProduct);

    // Map from RiceProduct entity to RiceProductResponse
    RiceProductResponse toRiceProductResponse(RiceProduct riceProduct, String message);

    // Map from RiceProduct entity to UpdateRiceProductResponse
    UpdateRiceProductResponse toUpdateRiceProductResponse(RiceProduct riceProduct, String message);

    // Map from RiceProductRegisterRequest to RiceProduct entity
    RiceProduct toRiceProduct(RiceProductRegisterRequest request);

    // Map from UpdateRiceProductRequest to an existing RiceProduct entity
    void updateRiceProductFromRequest(UpdateRiceProductRequest request, @MappingTarget RiceProduct riceProduct);

}

package com.ims.IMS.mapper;
import com.ims.IMS.api.warehouse.*;
import com.ims.IMS.exception.WarehouseNotFoundException;
import com.ims.IMS.model.warehouse.WareHouseData;
import com.ims.IMS.repository.WareHouse.WareHouseDataRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;

@Component
public class SatyRiceWarehouseMappingImpl implements SatyRiceWarehouseMapping {
    @Override
    public WareHouseDataResponse toWareHouseDataResponse(WareHouseData wareHouseData) {
        return new WareHouseDataResponse(
                wareHouseData.getId(),
                wareHouseData.getUniqueCodeWareHouse(),
                wareHouseData.getUniqueCodeProduct(),
                wareHouseData.getName(),
                wareHouseData.getPricePerKg(),
                wareHouseData.getStockQuantity(),
                wareHouseData.getInformationWareHouse(),
                wareHouseData.getAddress(),
                wareHouseData.getPhone()
        );
    }

    @Override
    public WareHouseDataUpdateResponse toWareHouseDataUpdateResponse(WareHouseData wareHouseData, String message) {
        return new WareHouseDataUpdateResponse(
                wareHouseData.getId(),
                wareHouseData.getUniqueCodeWareHouse(),
                wareHouseData.getUniqueCodeProduct(),
                wareHouseData.getName(),
                wareHouseData.getPricePerKg(),
                wareHouseData.getStockQuantity(),
                wareHouseData.getInformationWareHouse(),
                wareHouseData.getAddress(),
                wareHouseData.getPhone(),
                message
        );
    }

    @Override
    public WareHouseData toWareHouseData(WareHouseDataRegisterRequest request) {
        return WareHouseData.builder()
                .uniqueCodeWareHouse(request.uniqueCodeWareHouse())
                .uniqueCodeProduct(request.uniqueCodeProduct())
                .name(request.name())
                .pricePerKg(request.pricePerKg())
                .stockQuantity(request.stockQuantity())
                .informationWareHouse(request.informationWareHouse())
                .address(request.address())
                .phone(request.phone())
                .build();
    }

    @Override
    public WareHouseDetailResponse toWareHouseDetailResponse(WareHouseData wareHouseData) {
        return new WareHouseDetailResponse(
                wareHouseData.getId(),
                wareHouseData.getUniqueCodeWareHouse(),
                wareHouseData.getName(),
                wareHouseData.getPricePerKg(),
                wareHouseData.getStockQuantity(),
                wareHouseData.getInformationWareHouse(),
                wareHouseData.getAddress(),
                wareHouseData.getPhone()
        );
    }

    @Override
    public void updateWareHouseDataFromRequest(WareHouseDataUpdateRequest request, WareHouseData wareHouseData) {
        if (request.uniqueCodeWareHouse() != null) {
            wareHouseData.setUniqueCodeWareHouse(request.uniqueCodeWareHouse());
        }
        if (request.uniqueCodeProduct() != null) {
            wareHouseData.setUniqueCodeProduct(request.uniqueCodeProduct());
        }
        if (request.name() != null) {
            wareHouseData.setName(request.name());
        }
        if (request.pricePerKg() > 0) {
            wareHouseData.setPricePerKg(request.pricePerKg());
        }
        if (request.stockQuantity() > 0) {
            wareHouseData.setStockQuantity(request.stockQuantity());
        }
        if (request.informationWareHouse() != null) {
            wareHouseData.setInformationWareHouse(request.informationWareHouse());
        }
        if (request.address() != null) {
            wareHouseData.setAddress(request.address());
        }
        if (request.phone() != null) {
            wareHouseData.setPhone(request.phone());
        }
    }
}

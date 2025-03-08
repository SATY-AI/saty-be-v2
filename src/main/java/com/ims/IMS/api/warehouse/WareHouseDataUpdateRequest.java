package com.ims.IMS.api.warehouse;

public record WareHouseDataUpdateRequest(
        String uniqueCodeWareHouse,
        String uniqueCodeProduct,
        String name,
        double pricePerKg,
        int stockQuantity,
        String informationWareHouse,
        String address,
        String phone
) {}
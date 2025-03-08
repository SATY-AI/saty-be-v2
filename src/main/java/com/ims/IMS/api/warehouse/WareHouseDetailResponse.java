package com.ims.IMS.api.warehouse;

public record WareHouseDetailResponse(
        Integer id,
        String uniqueCodeWareHouse,
        String name,
        double pricePerKg,
        int stockQuantity,
        String informationWareHouse,
        String address,
        String phone
) {}
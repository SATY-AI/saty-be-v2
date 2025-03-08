package com.ims.IMS.api.customerRedeem;

public record CustomerRedeemResponse(
        Integer id,
        String username,
        String uniqueCodeProduct,
        String uniqueCodeWareHouse,
        String timeVerified,
        String messageTrungMua
) {}
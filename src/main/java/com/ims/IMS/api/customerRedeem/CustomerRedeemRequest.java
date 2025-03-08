package com.ims.IMS.api.customerRedeem;

public record CustomerRedeemRequest(
        String username,
        String password,
        String uniqueCodeProduct,
        String uniqueCodeWareHouse,
        String timeVerified,
        String messageTrungMua
) {}
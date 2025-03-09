package com.ims.IMS.api.customerRedeem;

public record CustomerRedeemRequest(
        String username,
        String uniqueCodeProduct,
        String uniqueCodeWareHouse,
        String messageTrungMua
) {}
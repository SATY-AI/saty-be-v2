package com.ims.IMS.api.qrProductInfo.CustomerRedeem;

public record CheckRedeemProductRequest(
        String codeProduct,
        String codeWarehouse,
        String codeCustomer,
        String username,
        String password
) {}
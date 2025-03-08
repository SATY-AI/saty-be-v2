package com.ims.IMS.api.qrProductInfo;

public record ProductInfoRequest(
        String codeProduct,
        String codeWarehouse
) {}
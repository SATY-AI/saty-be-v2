package com.ims.IMS.api.satyjourney.device;

public record AdminDeviceInformationResponse(
        String bienSoXe,
        String tenVN,
        String tenEn,
        String tenJP,
        String tenCN,
        String motaVN,
        String motaEN,
        String motaJP,
        String motaCN,
        Float kinhDo,
        Float viDo,
        String viTri
) {}

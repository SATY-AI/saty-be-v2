package com.ims.IMS.api.satyjourney.device;

public record SatyDeviceInformationRequest(
        Integer id,
        String bienSoXe, // Change to lowercase
        String Ten_VN,
        String Ten_EN,
        String Ten_JP,
        String Ten_CN,
        String MoTa_VN,
        String MoTa_EN,
        String MoTa_JP,
        String MoTa_CN,
        float KinhDo,
        float ViDo,
        String ViTri
) {}

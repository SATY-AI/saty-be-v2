package com.ims.IMS.api.device;

public record DeviceRegisterResponse(
        String deviceID,
        String description,
        String message
) {}

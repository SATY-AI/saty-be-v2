package com.ims.IMS.api.satyjourney.device;

public record SatyDeviceInformationCreateResponse(
        String message,
        String status,
        SatyDeviceInformationRequest satyDeviceInformationRequest
) {}

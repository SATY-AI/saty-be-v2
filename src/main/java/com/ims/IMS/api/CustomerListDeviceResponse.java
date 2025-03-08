package com.ims.IMS.api;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record CustomerListDeviceResponse(
        String deviceID,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime updated_at,
        String missionDescription,
        String locationX,
        String locationY,
        String locationID,
        String secretKey,
        String qrCodeBase64
) {}

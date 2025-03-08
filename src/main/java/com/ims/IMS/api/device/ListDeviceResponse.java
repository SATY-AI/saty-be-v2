package com.ims.IMS.api.device;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record ListDeviceResponse(
        Integer id,
        String deviceID,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime created_at,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime updated_at,
        String missionDescription,
        String locationX,
        String locationY,
        String locationID,
        String customerEmail, // New field for customer email
        String qrCodeBase64 // Add QR code field here
) {}

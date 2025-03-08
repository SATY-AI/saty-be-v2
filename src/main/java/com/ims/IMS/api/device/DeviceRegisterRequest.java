package com.ims.IMS.api.device;

import java.time.ZonedDateTime;

public record DeviceRegisterRequest(
        String deviceID,
        String description,
        ZonedDateTime created_at,
        ZonedDateTime updated_at,
        String missionDescription,
        String locationX,
        String locationY,
        String locationID // New field to link the device to a location
) {}

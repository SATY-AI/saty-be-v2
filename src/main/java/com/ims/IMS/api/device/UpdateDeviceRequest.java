package com.ims.IMS.api.device;

import java.time.ZonedDateTime;

public record UpdateDeviceRequest(
        String deviceID,
        String description,
        ZonedDateTime updated_at,
        String missionDescription,
        String locationX,
        String locationY,
        String locationID
) {}

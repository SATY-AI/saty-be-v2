package com.ims.IMS.api.location;

import com.ims.IMS.model.Enum.LocationProvince;

import java.time.ZonedDateTime;

public record UpdateLocationRequest(
        LocationProvince locationProvince,
        String locationID,
        String description,
        ZonedDateTime updatedAt,
        String urlLocationImage
) {}

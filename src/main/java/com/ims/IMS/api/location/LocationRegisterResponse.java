package com.ims.IMS.api.location;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ims.IMS.model.Enum.LocationProvince;

import java.time.ZonedDateTime;

public record LocationRegisterResponse(
        LocationProvince locationProvince,
        String locationID,
        String description,
        String urlLocationImage,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime updatedAt,
        String message
) {}

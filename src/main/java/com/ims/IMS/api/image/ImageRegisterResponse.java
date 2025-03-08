package com.ims.IMS.api.image;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record ImageRegisterResponse(
        String imageBucket,
        String description,
        String topic,
        String deviceID,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime created_at,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime updated_at
) {}

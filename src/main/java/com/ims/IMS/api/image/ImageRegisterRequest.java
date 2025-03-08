package com.ims.IMS.api.image;

import java.time.ZonedDateTime;
import java.util.List;

public record ImageRegisterRequest(
        String imageBucket,
        String description,
        String topic,
        List<String> imageList,
        String deviceID,
        ZonedDateTime created_at,
        ZonedDateTime updated_at
) {}

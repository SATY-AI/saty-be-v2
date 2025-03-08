package com.ims.IMS.api.image;

import java.time.ZonedDateTime;
import java.util.List;

public record UpdateImageRequest(
        String imageBucket,
        String description,
        String topic,
        List<String>imageList,
        String deviceID,
        ZonedDateTime updated_at
) {}

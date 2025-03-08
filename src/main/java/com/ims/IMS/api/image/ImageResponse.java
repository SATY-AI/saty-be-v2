package com.ims.IMS.api.image;

import java.util.List;

public record ImageResponse(
        String imageBucket,
        String description,
        String topic,
        List<String> imageList,
        String deviceID
) {}

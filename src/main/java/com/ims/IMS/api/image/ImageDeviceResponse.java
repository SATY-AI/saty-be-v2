package com.ims.IMS.api.image;

import java.util.List;

public record ImageDeviceResponse(
        String urlS3,
        String imageBucket,
        String description,
        String topic,
        String deviceID,
        List<String> imageList
) {}

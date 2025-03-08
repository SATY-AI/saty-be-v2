package com.ims.IMS.api.image;

import java.util.List;

public record ImageListDeviceResponse(
    String email,
    List<ImageResponse> images
) {}

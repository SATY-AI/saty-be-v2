package com.ims.IMS.api.image;

import java.util.List;

public record UpdateImageResponse(
        String message,
        List<String> imageList
) {}

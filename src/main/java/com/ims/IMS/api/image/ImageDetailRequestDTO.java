package com.ims.IMS.api.image;

import java.util.List;

public record ImageDetailRequestDTO(
        String deviceID,
        String imageName,
        List<String> description,
        List<String> number,
        List<String> length
) {}

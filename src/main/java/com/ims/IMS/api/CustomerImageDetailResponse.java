package com.ims.IMS.api;

import java.util.List;

public record CustomerImageDetailResponse(
        String imageName,
        List<String>description,
        List<String> number,
        List<String> length
) {}

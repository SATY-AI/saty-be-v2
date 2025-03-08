package com.ims.IMS.api;

import java.util.List;

public record CustomerDeviceResponse(
        String email,
        List<String> deviceID
) {}


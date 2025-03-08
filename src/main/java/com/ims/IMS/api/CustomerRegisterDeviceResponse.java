package com.ims.IMS.api;

import java.util.List;

public record CustomerRegisterDeviceResponse(
        String status,
        String email,
        List<String> addedDeviceIDs,
        List<String> failedDeviceIDs
) {}

package com.ims.IMS.api;

import java.util.HashSet;

public record CustomerRegisterDeviceRequest(
        String email,
        HashSet<String> deviceID
) {}

package com.ims.IMS.api.device;

import com.ims.IMS.model.imsprocessing.Device;

public record UpdateDeviceResponse(
        String message,
        Device updatedDevice
) {}

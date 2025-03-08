package com.ims.IMS.api.location;

import com.ims.IMS.api.device.ListDeviceResponse;

import java.util.List;

public record ListLocationResponse(
        Integer id,
        String locationID,
        String locationProvince,
        String description,
        String urlLocationImage,
        List<ListDeviceResponse> listDeviceResponse
) {}

package com.ims.IMS.api.location;

import com.ims.IMS.model.imsprocessing.Location;

public record UpdateLocationResponse(
        String message,
        Location location
) {}

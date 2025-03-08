package com.ims.IMS.model.imsprocessing;

import com.ims.IMS.model.Enum.InsectNumber;

public record InsectType (
        InsectNumber insectNumber,
        String description
){}


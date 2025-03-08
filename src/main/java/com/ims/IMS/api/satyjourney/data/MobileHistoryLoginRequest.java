package com.ims.IMS.api.satyjourney.data;

import java.time.ZonedDateTime;

public record MobileHistoryLoginRequest(
        String MaTaiXe,
        String BienSoXe,
        String NgayGioDangNhap,  // Keep as String
        String NgayGioDangXuat,
        String ToaDoDangXuat,
        String ToaDoDangNhap,
        String CODE
) {}

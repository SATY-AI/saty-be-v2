package com.ims.IMS.api.satyjourney.data;

import java.time.LocalDateTime;

public record GetDataDeoCaResponse(
        String customID,
        float KinhDo,
        float ViDo,
        float prvKinhDo,
        float prvViDo,
        float TocDo,
        Integer ACC,
        Integer Ex1,
        Integer Ex2,
        Integer MucNhienLieu,
        float NhietDo,
        float DoAm,
        Integer RSSI,
        LocalDateTime ThoiDiemLuuDuLieu,
        String RTC_Server,
        String RTC_Board,
        String MaTaiXe,
        Integer TrangThaiTaiXe,
        String SIM,
        String SDCard
) {}

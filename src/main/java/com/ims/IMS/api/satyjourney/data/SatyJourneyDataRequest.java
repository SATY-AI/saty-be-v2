package com.ims.IMS.api.satyjourney.data;

public record SatyJourneyDataRequest(
        String customID,
        String phoneNumber,
        float KinhDo,
        float ViDo,
        float TocDo,
        Integer ACC,
        Integer Ex1,
        Integer Ex2,
        Integer MucNhienLieu,
        float NhietDo,
        float DoAm,
        Integer RSSI,
        String RTC_Server,
        String RTC_Board,
        String MaTaiXe,
        Integer TrangThaiTaiXe,
        String SIM,
        String SDCard
) {}

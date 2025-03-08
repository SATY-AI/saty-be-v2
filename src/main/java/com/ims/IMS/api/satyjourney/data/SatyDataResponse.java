package com.ims.IMS.api.satyjourney.data;

public record SatyDataResponse(
         String customID,
         String PhoneNumber,
         Float KinhDo,
         Float ViDo,
         Float TocDo,
         Integer ACC,
         Integer Ex1,
         Integer Ex2,
         Integer MucNhienLieu,
         Float NhietDo,
         Float DoAm,
         Integer RSSI,
         String RTC_Server,
         String RTC_Board,
         String MaTaiXe,
         Integer TrangThaiTaiXe,
         String SIM,
         String SDCard
) {}
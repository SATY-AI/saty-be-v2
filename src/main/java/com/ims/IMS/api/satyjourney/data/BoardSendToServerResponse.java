package com.ims.IMS.api.satyjourney.data;

public record BoardSendToServerResponse(
        String statusCode,
        String MaTheNFC,
        String timeUpdate,
        String statusWorkingUpdate
) {}

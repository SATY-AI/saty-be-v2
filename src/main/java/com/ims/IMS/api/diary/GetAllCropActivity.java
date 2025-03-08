package com.ims.IMS.api.diary;

public record GetAllCropActivity(
        String idField,
        String job,
        String date,
        String type,
        String metadata,
        String cropSeasonId,
        String createdAt,
        String updatedAt
) {}

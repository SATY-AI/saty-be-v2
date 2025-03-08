package com.ims.IMS.api.diary;

public record GetCropActivityByIDResponse(
        String idField,
        String job,
        String date,
        String type,
        String metadata,
        String cropSeasonId,
        String createdAt,
        String updatedAt
) {}

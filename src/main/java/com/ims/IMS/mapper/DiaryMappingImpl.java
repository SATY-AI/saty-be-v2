package com.ims.IMS.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.IMS.api.diary.GetAllCropActivity;
import com.ims.IMS.api.diary.GetCropActivityByIDResponse;
import com.ims.IMS.model.diaryAgri.CropActivity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Primary
public class DiaryMappingImpl implements DiaryMapping {

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper instance

    @Override
    public GetAllCropActivity toGetAllCropActivity(CropActivity cropActivity) {
        if (cropActivity == null) {
            return null; // Handle null case if needed
        }
        String metadataString;
        try {
            // Convert the metadata Map back to a JSON string
            metadataString = objectMapper.writeValueAsString(cropActivity.getMetadata());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing metadata for GetAllCropActivity", e);
        }
        return new GetAllCropActivity(
                cropActivity.getIdField(),
                cropActivity.getJob(),
                cropActivity.getDate(),
                cropActivity.getType(),
                metadataString, // Use the serialized metadata string
                cropActivity.getCropSeasonId(),
                cropActivity.getCreatedAt(),
                cropActivity.getUpdatedAt()
        );
    }

    @Override
    public List<GetAllCropActivity> toGetAllCropActivityList(List<CropActivity> cropActivities) {
        if (cropActivities == null || cropActivities.isEmpty()) {
            return List.of(); // Return an empty list if input is null or empty
        }
        return cropActivities.stream()
                .map(this::toGetAllCropActivity) // Map each CropActivity to GetAllCropActivity
                .collect(Collectors.toList()); // Collect the results into a list
    }

    // New method to map CropActivity to GetCropActivityByIDResponse
    @Override
    public GetCropActivityByIDResponse toGetCropActivityByIDResponse(CropActivity cropActivity) {
        if (cropActivity == null) {
            return null; // Handle null case if needed
        }
        return new GetCropActivityByIDResponse(
                cropActivity.getIdField(),
                cropActivity.getJob(),
                cropActivity.getDate(),
                cropActivity.getType(),
                String.valueOf(cropActivity.getMetadata()), // Ensure this is properly formatted for your use case
                cropActivity.getCropSeasonId(),
                cropActivity.getCreatedAt(),
                cropActivity.getUpdatedAt()
        );
    }

}

package com.ims.IMS.service.diary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.IMS.api.diary.GetAllCropActivity;
import com.ims.IMS.api.diary.GetCropActivityByIDResponse;
import com.ims.IMS.mapper.DiaryMapping;
import com.ims.IMS.model.diaryAgri.CropActivity;
import com.ims.IMS.repository.CropDiary.CropActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CropActivityService {
    @Autowired
    private CropActivityRepository cropActivityRepository;
    @Autowired
    private DiaryMapping diaryMapping; // Inject the mapper
    private final ObjectMapper objectMapper = new ObjectMapper(); // Create an instance of ObjectMapper


    public List<GetAllCropActivity> findAll() {
        List<CropActivity> cropActivities = cropActivityRepository.findAll(); // Retrieve CropActivities
        return diaryMapping.toGetAllCropActivityList(cropActivities); // Map to GetAllCropActivity
    }

    public CropActivity save(CropActivity cropActivity) {
        return cropActivityRepository.save(cropActivity);
    }

    public Optional<GetCropActivityByIDResponse> getCropActivityByIdField(String idField) {
        Optional<CropActivity> cropActivity = cropActivityRepository.findByIdField(idField);
        return cropActivity.map(diaryMapping::toGetCropActivityByIDResponse);
    }
}

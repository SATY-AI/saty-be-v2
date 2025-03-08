package com.ims.IMS.mapper;

import com.ims.IMS.api.diary.GetAllCropActivity;
import com.ims.IMS.api.diary.GetCropActivityByIDResponse;
import com.ims.IMS.model.diaryAgri.CropActivity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", implementationName = "CoreDiaryMapper")
public interface DiaryMapping {
    // Maps CropActivity to GetAllCropActivity
    GetAllCropActivity toGetAllCropActivity(CropActivity cropActivity);
    // Maps a list of CropActivity to a list of GetAllCropActivity
    List<GetAllCropActivity> toGetAllCropActivityList(List<CropActivity> cropActivities);
    GetCropActivityByIDResponse toGetCropActivityByIDResponse(CropActivity cropActivity);
}
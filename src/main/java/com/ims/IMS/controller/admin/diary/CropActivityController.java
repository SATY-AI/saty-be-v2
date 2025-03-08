package com.ims.IMS.controller.admin.diary;

import com.ims.IMS.api.diary.GetAllCropActivity;
import com.ims.IMS.api.diary.GetCropActivityByIDResponse;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.mapper.DiaryMappingImpl;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.diary.CropActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crop/admin/season")
public class CropActivityController {

    @Autowired
    private CropActivityService cropActivityService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private DiaryMappingImpl diaryMapping;

    @GetMapping("/get-all-crop-activity")
    public ResponseApi<List<GetAllCropActivity>> getAll(@RequestHeader("Authorization") String token) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        List<GetAllCropActivity> cropActivityList = cropActivityService.findAll();
        return ResponseApi.success(cropActivityList);
    }

    @GetMapping("/get-crop-activity/{id}")
    public ResponseApi<GetCropActivityByIDResponse> getCropActivityByID(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) { // Retrieve ID from the path
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Call the service method to get the crop activity by ID
        Optional<GetCropActivityByIDResponse> getCropActivityByIDResponse = cropActivityService.getCropActivityByIdField(id);
        // Return the appropriate response
        if (getCropActivityByIDResponse.isPresent()) {
            return ResponseApi.success(getCropActivityByIDResponse.get());
        } else {
            return ResponseApi.error("400","Crop activity not found");
        }
    }


}

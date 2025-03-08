package com.ims.IMS.controller.admin.crud_fertilizer;


import com.ims.IMS.api.fertilizer.request.AddFertilizerRequest;
import com.ims.IMS.api.fertilizer.request.UpdateFertilizerRequest;
import com.ims.IMS.api.fertilizer.response.AddFertilizerStatusResponse;
import com.ims.IMS.api.fertilizer.response.DeleteFertilizerStatus;
import com.ims.IMS.api.fertilizer.response.GetAllFertilizerResponse;
import com.ims.IMS.api.fertilizer.response.UpdateFertilizerStatus;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.model.fertilizerProduct.Fertilizer;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.fertilizer.FertilizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ims/admin")
public class FertilizerCrud {
    @Autowired
    private FertilizerService fertilizerService;
    @Autowired
    private AdminService adminService;

    // Get all fertilizers
    @GetMapping("/fertilizer/all")
    public ResponseApi<List<GetAllFertilizerResponse>> getAllFertilizers(@RequestHeader("Authorization") String token) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        return ResponseApi.success(fertilizerService.getAllFertilizers());
    }

    // Get fertilizer by ID
    @GetMapping("/fertilizer/{id}")
    public ResponseApi<GetAllFertilizerResponse> getFertilizerById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        Optional<GetAllFertilizerResponse> response = fertilizerService.getFertilizerById(id);
        // Return success or error using the ResponseApi
        return response.map(fertilizer -> ResponseApi.success(fertilizer))
                .orElseGet(() -> ResponseApi.error("NOT_FOUND", "Fertilizer with ID " + id + " not found"));
    }

    // Update an existing fertilizer
    @PutMapping("/fertilizer/{id}")
    public ResponseApi<UpdateFertilizerStatus> updateFertilizer(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody UpdateFertilizerRequest request) {
        // Update the fertilizer using the service
        Fertilizer updatedFertilizer = fertilizerService.updateFertilizer(id, request);
        // Return success response with status message
        UpdateFertilizerStatus status = new UpdateFertilizerStatus("Fertilizer updated successfully");
        return ResponseApi.success(status);
    }

    // Delete fertilizer by ID
    @DeleteMapping("/fertilizer/{id}")
    public ResponseApi<DeleteFertilizerStatus> deleteFertilizer(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        // Delete the fertilizer using the service
        boolean isDeleted = fertilizerService.deleteFertilizerById(id);
        if (isDeleted) {
            // Return success response with status message
            DeleteFertilizerStatus status = new DeleteFertilizerStatus("Fertilizer deleted successfully");
            return ResponseApi.success(status);
        } else {
            // Return error response if the fertilizer wasn't found or couldn't be deleted
            return ResponseApi.error("NOT_FOUND", "Fertilizer with ID " + id + " not found");
        }
    }

    // Add new fertilizer
    @PostMapping("/fertilizer")
    public ResponseApi<AddFertilizerStatusResponse> addFertilizer(
            @RequestHeader("Authorization") String token,
            @RequestBody AddFertilizerRequest request) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Call service method to add the fertilizer
        Fertilizer addedFertilizer = fertilizerService.addFertilizer(request);
        // Create the response using AddFertilizerStatusResponse
        AddFertilizerStatusResponse response = new AddFertilizerStatusResponse(
                "success", // Status of the operation
                "Fertilizer added successfully with ID " + addedFertilizer.getId(), // Detailed message
                addedFertilizer.getId(), // ID of the added fertilizer
                request // Pass the original request
        );
        return ResponseApi.success(response);
    }


}

package com.ims.IMS.controller.saty_journey.admin;

import com.ims.IMS.api.satyjourney.device.SatyDeviceInformationCreateResponse;
import com.ims.IMS.api.satyjourney.device.SatyDeviceInformationRequest;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.mapper.SatyJourneyDataMapperImpl;
import com.ims.IMS.model.SatyJourney.SatyDevice;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.service.AdminService;
import com.ims.IMS.service.satyJourney.SatyDeviceService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/journey/admin")
public class SatyDeviceAdminController {
    @Autowired
    private SatyDeviceService satyDeviceService;
    @Autowired
    private SatyJourneyDataMapperImpl satyJourneyDataMapper;
    @Autowired
    private AdminService adminService;
    // Create a new SatyDevice
    @PostMapping("/device")
    public ResponseApi<SatyDeviceInformationCreateResponse> createSatyDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody SatyDeviceInformationRequest satyDeviceInformationRequest) {
        // Step 1: Validate token and retrieve admin data
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        try {
            // Step 2: Map the request DTO to the entity
            SatyDevice satyDevice = satyJourneyDataMapper.toEntity(satyDeviceInformationRequest);
            // Step 3: Call the service to save the device
            SatyDevice createdDevice = satyDeviceService.createSatyDevice(satyDevice);
            // Step 4: Return the success response
            SatyDeviceInformationCreateResponse response = new SatyDeviceInformationCreateResponse(
                    "Saty device created successfully",
                    "SUCCESS",
                    satyDeviceInformationRequest
            );
            return ResponseApi.success(response);
        } catch (IllegalArgumentException e) {
            // Handle the case where bienSoXe is not unique
            SatyDeviceInformationCreateResponse response = new SatyDeviceInformationCreateResponse(
                    e.getMessage(),
                    "ERROR",
                    satyDeviceInformationRequest
            );
            return ResponseApi.error("01", response.toString());
        }
    }

    @GetMapping("/device/{id}")
    public ResponseApi<SatyDeviceInformationRequest> getSatyDeviceById(@PathVariable Integer id) {
        Optional<SatyDevice> optionalDevice = satyDeviceService.getSatyDeviceById(id);
        // Check if the device exists
        if (optionalDevice.isPresent()) {
            // Map the found device to the response
            SatyDeviceInformationRequest response = satyJourneyDataMapper.toRequest(optionalDevice.get());
            return ResponseApi.success(response);
        } else {
            // Return an error response if not found
            return ResponseApi.error("01", "NOT_FOUND");
        }
    }

    @PutMapping("/device/{id}")
    public ResponseApi<SatyDeviceInformationCreateResponse> updateSatyDevice(
            @PathVariable Integer id,
            @RequestBody SatyDeviceInformationRequest satyDeviceInformationRequest) {
        // Map the request DTO to the entity
        SatyDevice updatedDevice = satyJourneyDataMapper.toEntity(satyDeviceInformationRequest);
        // Call service to update the device, which returns an Optional
        Optional<SatyDevice> optionalUpdatedDevice = satyDeviceService.updateSatyDevice(id, updatedDevice);
        // Check if the update was successful (device exists and was updated)
        if (optionalUpdatedDevice.isPresent()) {
            // Build response message
            SatyDeviceInformationCreateResponse response = new SatyDeviceInformationCreateResponse(
                    "Saty device updated successfully",
                    "SUCCESS",
                    satyDeviceInformationRequest
            );
            return ResponseApi.success(response);
        } else {
            // Return an error if the device was not found
            return ResponseApi.error("Device not found for update", "NOT_FOUND");
        }
    }

    // Delete a SatyDevice by ID
    @DeleteMapping("/device/{id}")
    public ResponseApi<String> deleteSatyDevice(@RequestHeader("Authorization") String token,
                                                @PathVariable Integer id) {
        // Step 1: Validate token and retrieve admin data (optional for authorization)
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        // Step 2: Check if the device exists
        Optional<SatyDevice> optionalDevice = satyDeviceService.getSatyDeviceById(id);
        if (optionalDevice.isPresent()) {
            // Step 3: Delete the device
            satyDeviceService.deleteSatyDeviceById(id);
            // Step 4: Return a success response
            return ResponseApi.success("Saty device " + id + " deleted successfully");
        } else {
            // Step 5: Return an error response if the device is not found
            return ResponseApi.error("01", "Device not found");
        }
    }

}

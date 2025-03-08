package com.ims.IMS.controller.saty_journey.customer;

import com.ims.IMS.api.satyjourney.data.SatyDataRequest;
import com.ims.IMS.api.satyjourney.data.SatyDataResponse;
import com.ims.IMS.api.satyjourney.device.SatyDeviceInformationResponse;
import com.ims.IMS.api.satyjourney.device.SatyDeviceResponse;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.mapper.ImsMapping;
import com.ims.IMS.model.SatyJourney.Data2Board;
import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import com.ims.IMS.repository.Journey.Data2BoardRepository;
import com.ims.IMS.repository.Journey.SatyJourneyDataRepository;
import com.ims.IMS.service.satyJourney.SatyDeviceService;
import com.ims.IMS.service.satyJourney.SatyJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/journey")
public class SatyJourneyDeviceController {
    @Autowired
    private SatyJourneyService satyJourneyService;
    @Autowired
    private SatyJourneyDataRepository deoCaDataRepository;
    @Autowired
    private ImsMapping imsMapping;
    @Autowired
    private Data2BoardRepository data2BoardRepository;
    @Autowired
    private SatyDeviceService satyDeviceService;

    private void validateInput(SatyDataRequest satyDataRequest) {
        if (satyDataRequest.ID() == null || satyDataRequest.ID().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        }
        if (satyDataRequest.PhoneNumber() == null || !satyDataRequest.PhoneNumber().matches("\\d{10}")) {
            throw new IllegalArgumentException("PhoneNumber must be a valid 10-digit number.");
        }
        if (satyDataRequest.S() == null || satyDataRequest.S().isEmpty()) {
            throw new IllegalArgumentException("S cannot be null or empty.");
        }
    }

    @PostMapping("/saty-journey-upload-data")
    public ResponseApi<SatyDeviceResponse> submitDataFromDeviceDeoCa(@RequestBody SatyDataRequest satyDataRequest) {
        try {
            // Validate input
            validateInput(satyDataRequest);
            // Map SatyDataRequest to SatyJourneyData
            SatyJourneyData deoCaData = imsMapping.mapToSatyJourneyData(satyDataRequest);
            // Save the SatyJourneyData to the saty_journey_data table
            satyJourneyService.saveDeoCaData(deoCaData);
            // Check if customID exists in SatyJourneyData, and save it to data2board
            String customID = deoCaData.getCustomID();
            String configDevice = "0XX"; // Default value
            if (customID != null && !customID.isEmpty()) {
                Data2Board data2Board = Data2Board.builder()
                        .customID(customID)
                        .S(configDevice)  // Assuming there's a value for 'S' in the request
                        .build();
                data2BoardRepository.save(data2Board); // Save the Data2Board record
            }
            // Return success response
            SatyDeviceResponse response = new SatyDeviceResponse("Data Successfully saved");
            return ResponseApi.success(response);
        } catch (IllegalArgumentException ex) {
            return ResponseApi.error("400", "Validation error: " + ex.getMessage());
        } catch (DataAccessException ex) {
            return ResponseApi.error("400", "Database error: " + ex.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions and return an error response
            return ResponseApi.error("400", "Failed to save data: " + e.getMessage());
        }
    }

    @GetMapping("/saty-journey-data")
    public ResponseApi<List<SatyDataResponse>> getDataByCustomIDAndPhoneNumber(
            @RequestParam String ID,
            @RequestParam String PhoneNumber) {
        // Check if both customID and PhoneNumber exist
        if (!satyJourneyService.hasCustomID(ID)) {
            return ResponseApi.error("03", "No data found for customID: " + ID);
        }
        if (!satyJourneyService.hasPhoneNumber(PhoneNumber)) {
            return ResponseApi.error("04", "No data found for phoneNumber: " + PhoneNumber);
        }
        // If both exist, fetch the list of matching satyJourneyService
        List<SatyJourneyData> satyJourneyDataList = satyJourneyService.getDeoCaDataByCustomIdAndPhoneNumber(ID, PhoneNumber);

        // Sort the data to get the latest entries (assuming you have a timestamp or similar field to sort by)
        List<SatyJourneyData> sortedData = satyJourneyDataList.stream()
                .sorted(Comparator.comparing(SatyJourneyData::getRtcServer).reversed()) // Assuming getTimestamp() returns the timestamp
                .limit(20)
                .collect(Collectors.toList());
        if (!sortedData.isEmpty()) {
            // Map each DeoCaData to DeoCaDataResponse
            List<SatyDataResponse> responseList = sortedData.stream()
                    .map(imsMapping::mapToSatyDataResponse)
                    .collect(Collectors.toList());
            // Return success with the found data
            return ResponseApi.success(responseList);
        } else {
            // Return error if no data matches the customID and phoneNumber combination
            return ResponseApi.error("02", "No data found for customID: " + ID + " and phoneNumber: " + PhoneNumber);
        }
    }

    @GetMapping("/saty-journey-data/sorted")
    public ResponseApi<List<SatyDataResponse>> getAllSortedData(
            @RequestParam(required = false) String PhoneNumber,
            @RequestParam(required = false) String customID,
            @RequestParam(required = false) Float KinhDo,
            @RequestParam(required = false) Float ViDo,
            @RequestParam(required = false) Float TocDo,
            @RequestParam(required = false) Integer ACC,
            @RequestParam(required = false) Integer Ex1,
            @RequestParam(required = false) Integer Ex2,
            @RequestParam(required = false) Integer MucNhienLieu,
            @RequestParam(required = false) Float NhietDo,
            @RequestParam(required = false) Float DoAm,
            @RequestParam(required = false) Integer RSSI,
            @RequestParam(required = false) String RTC_Server,
            @RequestParam(required = false) String MaTaiXe,
            @RequestParam(required = false) Integer TrangThaiTaiXe,
            @RequestParam(required = false) String SIM,
            @RequestParam(required = false) String SDCard,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageOffset) {
        try {
            // Ensure pageSize is at least 10
            if (pageSize < 10) {
                pageSize = 10;
            }

            // Fetch filtered data from the service
            List<SatyJourneyData> deoCaDataList = satyJourneyService.getFilteredDeoCaData(
                    PhoneNumber, customID, KinhDo, ViDo, TocDo, ACC, Ex1, Ex2, MucNhienLieu, NhietDo, DoAm, RSSI,
                    RTC_Server, MaTaiXe, TrangThaiTaiXe, SIM, SDCard, pageSize, pageOffset
            );
            // Handle the case where no data matches the filters
            if (deoCaDataList.isEmpty()) {
                return ResponseApi.error("01", "No matching data found based on the provided filters.");
            }
            // Map each DeoCaData to DeoCaDataResponse
            List<SatyDataResponse> responseList = deoCaDataList.stream()
                    .map(imsMapping::mapToSatyDataResponse)
                    .collect(Collectors.toList());
            // Return the successfully mapped data
            return ResponseApi.success(responseList);
        } catch (IllegalArgumentException e) {
            // Handle invalid arguments or filter values (e.g., invalid numbers)
            return ResponseApi.error("02", "Invalid request parameters: " + e.getMessage());
        } catch (DataAccessException e) {
            // Handle database-related issues
            return ResponseApi.error("03", "Database access error: " + e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseApi.error("04", "Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/saty-device-info")
    public ResponseApi<SatyDeviceInformationResponse> getDeviceInformation(
            @RequestParam String bienSoXe
    ) {
        Optional<SatyDeviceInformationResponse> deviceInfo = satyDeviceService.getDeviceInformation(bienSoXe);
        return deviceInfo.map(ResponseApi::success)
                .orElseGet(() -> ResponseApi.error("0", "No device information found."));
    }



}

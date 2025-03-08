package com.ims.IMS.service.fertilizer;

import com.ims.IMS.api.fertilizer.request.FertilizerUsageLogRequest;
import com.ims.IMS.model.SatyJourney.SatyDevice;
import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import com.ims.IMS.model.fertilizerProduct.Fertilizer;
import com.ims.IMS.model.fertilizerProduct.FertilizerUsageLog;
import com.ims.IMS.model.progress_plant.ProgressPlant;
import com.ims.IMS.repository.Fertilizer.FertilizerUsageLogRepository;
import com.ims.IMS.repository.ProgressPlantRepository;
import com.ims.IMS.repository.Journey.SatyDeviceRepository;
import com.ims.IMS.service.satyJourney.SatyJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FertilizerUsageLogService {
    @Autowired
    private FertilizerUsageLogRepository fertilizerUsageLogRepository;
    @Autowired
    private SatyJourneyService satyJourneyService;
    @Autowired
    private FertilizerService fertilizerService; // Service to fetch Fertilizer
    // Save fertilizer usage log and plant progress
    @Autowired
    private ProgressPlantRepository progressPlantRepository;
    @Autowired
    private SatyDeviceRepository satyDeviceRepository;

    public FertilizerUsageLog saveFertilizerUsageLog(FertilizerUsageLogRequest request) {
        // Fetch SatyJourneyData using phone number
        SatyJourneyData satyJourneyData = satyJourneyService.findByPhoneNumber(request.satyJourneyData().phoneNumber());
        // Fetch the Fertilizer using the unique code
        Optional<Fertilizer> optionalFertilizer = fertilizerService.findByUniqueCode(request.addFertilizerRequest().uniqueCode());
        if (!optionalFertilizer.isPresent()) {
            throw new RuntimeException("Fertilizer not found with unique code: " + request.addFertilizerRequest().uniqueCode());
        }
        Fertilizer fertilizer = optionalFertilizer.get();
        // Create FertilizerUsageLog entity
        FertilizerUsageLog fertilizerUsageLog = new FertilizerUsageLog(
                null,
                satyJourneyData,
                fertilizer,
                request.amountUsed(),
                request.dateUsed()
        );
        FertilizerUsageLog savedLog = fertilizerUsageLogRepository.save(fertilizerUsageLog);

        // Fetch the existing SatyDevice from the repository
        SatyDevice existingDevice = request.progressPlantRequest().device();

        // Check if the device is not null and exists in the repository
        if (existingDevice != null && existingDevice.getBienSoXe() != null) {
            // Fetch the device from the database using its ID
            Optional<SatyDevice> optionalDevice = satyDeviceRepository.findByBienSoXe(existingDevice.getBienSoXe());
            if (!optionalDevice.isPresent()) {
                throw new RuntimeException("SatyDevice not found with ID: " + existingDevice.getId());
            }
            existingDevice = optionalDevice.get(); // Get the existing device
        } else {
            throw new RuntimeException("SatyDevice information is incomplete.");
        }

        // Create and save ProgressPlant entity
        ProgressPlant progressPlant = ProgressPlant.builder()
                .plantName(request.progressPlantRequest().plantName())
                .plantType(request.progressPlantRequest().plantType())
                .height(request.progressPlantRequest().height())
                .plantingDate(request.progressPlantRequest().plantingDate())
                .growthStage(request.progressPlantRequest().growthStage())
                .isPerennial(request.progressPlantRequest().isPerennial())
                .fertilizerUsageLog(savedLog)
                .device(existingDevice) // Use the existing device
                .build();

        progressPlantRepository.save(progressPlant);

        return savedLog;
    }



    public List<FertilizerUsageLog> getUsageLogByPhoneNumber(String phoneNumber) {
        return fertilizerUsageLogRepository.findBySatyJourneyData_PhoneNumber(phoneNumber);
    }
}

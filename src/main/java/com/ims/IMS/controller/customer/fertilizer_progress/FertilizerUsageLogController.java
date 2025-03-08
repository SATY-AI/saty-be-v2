package com.ims.IMS.controller.customer.fertilizer_progress;

import com.ims.IMS.api.fertilizer.request.FertilizerUsageLogRequest;
import com.ims.IMS.api.fertilizer.response.FertilizerUsageLogResponse;
import com.ims.IMS.api.satyjourney.data.SatyJourneyDataRequest;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.mapper.SatyJourneyDataMapper;
import com.ims.IMS.model.fertilizerProduct.FertilizerUsageLog;
import com.ims.IMS.service.fertilizer.FertilizerUsageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ims/customer")
public class FertilizerUsageLogController {
    @Autowired
    private FertilizerUsageLogService fertilizerUsageLogService;
    @Autowired
    private SatyJourneyDataMapper satyJourneyDataMapper;
    // POST request to save fertilizer usage log and plant progress
    @PostMapping("/save-log-usage")
    public ResponseApi<FertilizerUsageLogResponse> saveFertilizerUsageLog(@RequestBody FertilizerUsageLogRequest request) {
        // Call service to save fertilizer usage log
        FertilizerUsageLog savedLog = fertilizerUsageLogService.saveFertilizerUsageLog(request);
        SatyJourneyDataRequest satyJourneyDataRequest = satyJourneyDataMapper.toSatyJourneyDataRequest(savedLog.getSatyJourneyData());
        // Create a response (optional, you can map savedLog to a response DTO)
        FertilizerUsageLogResponse response = new FertilizerUsageLogResponse(
                satyJourneyDataRequest,
                savedLog.getAmountUsed(),
                savedLog.getDateUsed());
        // Return a success response with the saved log
        return ResponseApi.success(response);
    }
}

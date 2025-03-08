package com.ims.IMS.controller.customer.plant_progress;


import com.ims.IMS.service.fertilizer.FertilizerUsageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class PlantCropsLogController {
    @Autowired
    private FertilizerUsageLogService fertilizerUsageLogService;
}

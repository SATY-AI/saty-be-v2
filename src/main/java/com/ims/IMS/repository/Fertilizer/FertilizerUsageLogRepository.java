package com.ims.IMS.repository.Fertilizer;

import com.ims.IMS.model.fertilizerProduct.FertilizerUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface FertilizerUsageLogRepository extends JpaRepository<FertilizerUsageLog, Integer> {
    List<FertilizerUsageLog> findBySatyJourneyData_PhoneNumber(String phoneNumber);
}


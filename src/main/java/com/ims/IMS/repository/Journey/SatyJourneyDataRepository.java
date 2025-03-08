package com.ims.IMS.repository.Journey;

import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface SatyJourneyDataRepository extends JpaRepository<SatyJourneyData, Integer> {
    List<SatyJourneyData> findByCustomID(@Param("customID") String customID);
    List<SatyJourneyData> findByPhoneNumber(@Param("PhoneNumber") String PhoneNumber);
    Optional<SatyJourneyData> findByMaTaiXe(String maTaiXe);
    List<SatyJourneyData> findByCustomIDAndPhoneNumber(@Param("customID") String customID, @Param("phoneNumber") String phoneNumber);
}

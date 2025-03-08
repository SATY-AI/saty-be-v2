package com.ims.IMS.repository.Journey;

import com.ims.IMS.model.SatyJourney.SatyDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@RepositoryRestResource
@Repository
public interface SatyDeviceRepository extends JpaRepository<SatyDevice, Integer> {
    Optional<SatyDevice> findByBienSoXe(String bienSoXe);
    boolean existsByBienSoXe(String bienSoXe);
}

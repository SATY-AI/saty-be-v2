package com.ims.IMS.repository;

import com.ims.IMS.model.imsprocessing.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByLocationID(String deviceID);
    Optional<Location> findById (Integer id);
    boolean existsByLocationID(String locationID);
}

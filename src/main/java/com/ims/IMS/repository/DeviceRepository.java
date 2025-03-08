package com.ims.IMS.repository;

import com.ims.IMS.model.imsprocessing.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Optional<Device> findByDeviceID(String deviceID);
    Optional<Device> findById (Integer id);
    boolean existsByDeviceID(String deviceID);
    List<Device> findByLocationID(String locationID);
}

package com.ims.IMS.repository;


import com.ims.IMS.model.CusDev.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CustomerDeviceRepository extends JpaRepository<CustomerDevice, Integer> {
    Optional<CustomerDevice> findByEmail(String email);
    Optional<CustomerDevice> findById(Integer id);
    boolean existsByEmail(String email);

}

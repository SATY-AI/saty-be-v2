package com.ims.IMS.repository.Fertilizer;


import com.ims.IMS.model.fertilizerProduct.Fertilizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface FertilizerRepository extends JpaRepository<Fertilizer, Long> {
    Optional<Fertilizer> findByUniqueCode(String uniqueCode);
}

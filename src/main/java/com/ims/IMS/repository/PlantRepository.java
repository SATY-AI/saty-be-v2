package com.ims.IMS.repository;

import com.ims.IMS.model.plantProduct.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Integer> {

}

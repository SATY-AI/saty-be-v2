package com.ims.IMS.repository.WareHouse;

import com.ims.IMS.model.warehouse.WareHouseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface WareHouseDataRepository extends JpaRepository<WareHouseData, Integer> {

    List<WareHouseData> findAllByUniqueCodeWareHouse(String uniqueCodeWareHouse);

    Optional<WareHouseData> findByUniqueCodeProduct(String uniqueCodeProduct);

    boolean existsByUniqueCodeWareHouse(String uniqueCodeWareHouse);

    boolean existsByUniqueCodeProduct(String uniqueCodeProduct);

    boolean existsByUniqueCodeWareHouseAndUniqueCodeProduct(String uniqueCodeWareHouse, String uniqueCodeProduct);

}
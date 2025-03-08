package com.ims.IMS.repository.Rice;

import com.ims.IMS.model.riceProduct.RiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiceProductRepository extends JpaRepository<RiceProduct, Integer> {
    // Additional query methods can be defined here if needed
    Optional<RiceProduct> findByUniqueCode(String uniqueCode);
    boolean existsByUniqueCode(String uniqueCode);
}
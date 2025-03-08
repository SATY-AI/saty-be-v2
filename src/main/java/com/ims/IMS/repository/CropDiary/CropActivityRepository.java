package com.ims.IMS.repository.CropDiary;

import com.ims.IMS.model.diaryAgri.CropActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CropActivityRepository extends JpaRepository<CropActivity, Long> {
    // Additional query methods can be defined here if needed
    // Custom query method to find a CropActivity by ID
    Optional<CropActivity> findByIdField(String idField);
}

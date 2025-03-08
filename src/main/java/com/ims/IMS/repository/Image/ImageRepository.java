package com.ims.IMS.repository.Image;

import com.ims.IMS.model.imsprocessing.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByDeviceID(String deviceID);  // Updated to return List<Image>
    List<Image> findByDeviceIDIn(Set<String> deviceIDs); // Query by list of deviceIDs
    Optional<Image> findByImageBucket(String imageBucket);
    boolean existsByImageBucket(String imageBucket); // Corrected method name
    Optional<Image> findById (Integer id);
}

package com.ims.IMS.repository.Image;


import com.ims.IMS.model.imsprocessing.ImageDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ImageDetailRepository extends JpaRepository<ImageDetail, Integer> {
    List<ImageDetail> findByImageName(String urlImage);
}

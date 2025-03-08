package com.ims.IMS.repository;


import com.ims.IMS.model.insect.InsectListData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface InsectListDataRepository extends JpaRepository<InsectListData, Long> {


}

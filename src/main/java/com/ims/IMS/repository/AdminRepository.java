package com.ims.IMS.repository;
import com.ims.IMS.common.Role;
import com.ims.IMS.model.groupuser.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);
    Admin findByPhoneNumber(String phoneNumber);
    Admin findByEmailAndRole(String email, Role role);
}

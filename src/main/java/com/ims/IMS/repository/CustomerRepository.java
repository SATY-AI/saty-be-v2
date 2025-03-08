package com.ims.IMS.repository;

import com.ims.IMS.common.Role;
import com.ims.IMS.model.groupuser.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findById (Integer id);
    Customer findByEmail(String email);
    Customer findByPhoneNumber(String phonenumber);
    Customer findByPassword (String password);
    Customer findByEmailAndRole(String email, Role role);
}

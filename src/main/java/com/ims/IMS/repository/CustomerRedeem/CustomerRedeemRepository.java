package com.ims.IMS.repository.CustomerRedeem;

import com.ims.IMS.model.CustomerRedeem.CustomerRedeem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRedeemRepository extends JpaRepository<CustomerRedeem, Integer> {

}
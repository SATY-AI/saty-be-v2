package com.ims.IMS.mapper;

import com.ims.IMS.api.customerRedeem.CustomerRedeemRequest;
import com.ims.IMS.api.customerRedeem.CustomerRedeemResponse;
import com.ims.IMS.model.CustomerRedeem.CustomerRedeem;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class SatyCustomerRedeemMappingImpl implements SatyCustomerRedeemMapping {
    @Override
    public CustomerRedeem toEntity(CustomerRedeemRequest request) {
        if (request == null) {
            return null;
        }
        return CustomerRedeem.builder()
                .username(request.username())
                .password(request.password())
                .uniqueCodeProduct(request.uniqueCodeProduct())
                .uniqueCodeWareHouse(request.uniqueCodeWareHouse())
                .timeVerified(ZonedDateTime.parse(request.timeVerified()))
                .build();
    }

    @Override
    public CustomerRedeemResponse toResponse(CustomerRedeem customerRedeem) {
        if (customerRedeem == null) {
            return null;
        }
        return new CustomerRedeemResponse(
                customerRedeem.getId(),
                customerRedeem.getUsername(),
                customerRedeem.getUniqueCodeProduct(),
                customerRedeem.getUniqueCodeWareHouse(),
                String.valueOf(customerRedeem.getTimeVerified()),
                "Message Trung Mua"
        );
    }

    @Override
    public CustomerRedeem updateEntity(CustomerRedeem entity, CustomerRedeemRequest request) {
        if (entity == null || request == null) {
            return null;
        }
        entity.setUsername(request.username());
        entity.setPassword(request.password());
        entity.setUniqueCodeProduct(request.uniqueCodeProduct());
        entity.setUniqueCodeWareHouse(request.uniqueCodeWareHouse());
        entity.setTimeVerified(ZonedDateTime.parse(request.timeVerified()));
        entity.setMessageTrungMua(request.messageTrungMua());
        return entity;
    }
}

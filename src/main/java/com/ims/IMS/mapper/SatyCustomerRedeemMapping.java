package com.ims.IMS.mapper;
import com.ims.IMS.api.customerRedeem.CustomerRedeemRequest;
import com.ims.IMS.api.customerRedeem.CustomerRedeemResponse;
import com.ims.IMS.model.CustomerRedeem.CustomerRedeem;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
public interface SatyCustomerRedeemMapping {

    @Mapping(target = "id", ignore = true)
    com.ims.IMS.model.CustomerRedeem.CustomerRedeem toEntity(CustomerRedeemRequest request);

    CustomerRedeemResponse toResponse(CustomerRedeem customerRedeem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerRedeem updateEntity(@MappingTarget CustomerRedeem entity, CustomerRedeemRequest request);

}

package com.ims.IMS.model.CusDev;


import com.ims.IMS.api.CustomerRegisterDeviceRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "customer_device")
public class CustomerDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private HashSet<String> deviceID;

    public static CustomerDevice fromAdminCreate(CustomerRegisterDeviceRequest customerRegisterDeviceRequest) {
        return CustomerDevice.builder()
                .email(customerRegisterDeviceRequest.email())
                .deviceID(customerRegisterDeviceRequest.deviceID())
                .build();
    }
}

package com.ims.IMS.model.CustomerRedeem;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "customer_redeem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRedeem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String uniqueCodeProduct;
    private String uniqueCodeWareHouse;
    private ZonedDateTime timeVerified;
    //Message Trung Mua
    private String messageTrungMua;
}
package com.ims.IMS.model.warehouse;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "warehouse_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WareHouseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uniqueCodeWareHouse;
    private String uniqueCodeProduct;
    private String name;
    private double pricePerKg;
    private int stockQuantity;
    private String informationWareHouse;
    private String address;
    private String phone;
}

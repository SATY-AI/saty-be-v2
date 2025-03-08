package com.ims.IMS.model.fertilizerProduct;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fertilizer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fertilizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uniqueCode;
    private String name;
    private double pricePerKg;
    private int stockQuantity;
    private String description;
    private String manufacturer;
}

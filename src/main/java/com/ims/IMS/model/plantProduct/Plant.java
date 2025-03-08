package com.ims.IMS.model.plantProduct;

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
@Table(name = "plant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;           // Name of the plant
    private String type;           // Type of the plant (e.g., Flowering, Non-Flowering)
    private Double height;         // Height of the plant in centimeters
    private Double price;          // Price of the plant in dollars
    private String fertilizerRecommendations; // Suggested fertilizers

}
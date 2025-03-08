package com.ims.IMS.model.insect;


import com.ims.IMS.model.Enum.InsectRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "insects")
@Data // Using Lombok to generate getters, setters, etc.
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scientific_name", nullable = false)
    private String scientificName;

    @Column(name = "common_name")
    private String commonName;

    @Column(name = "family")
    private String family;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "description")
    private String description;

    @Column(name = "habitat")
    private String habitat;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private InsectRole role;

    @Column(name = "damage_symptoms")
    private String damageSymptoms;

    @Column(name = "control_measures")
    private String controlMeasures;

}

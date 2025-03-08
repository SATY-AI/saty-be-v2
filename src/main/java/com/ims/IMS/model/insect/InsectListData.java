package com.ims.IMS.model.insect;

import jakarta.persistence.Column;
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
@Table(name = "insects_list")
@Data // Using Lombok to generate getters, setters, etc.
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsectListData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vnName")
    private String vnName;

    @Column(name = "enName")
    private String enName;
}

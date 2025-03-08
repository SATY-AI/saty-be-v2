package com.ims.IMS.model.SatyJourney;


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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "saty_device")
public class SatyDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "BienSoXe", unique = true, nullable = false) // Ensure this field is unique
    private String bienSoXe;
    private String Ten_VN;
    private String Ten_EN;
    private String Ten_JP;
    private String Ten_CN;
    private String MoTa_VN;
    private String MoTa_EN;
    private String MoTa_JP;
    private String MoTa_CN;
    private float KinhDo;
    private float ViDo;
    private String ViTri;
}
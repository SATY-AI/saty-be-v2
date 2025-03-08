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

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "history_journey_login")
public class HistoryBoardLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MaTaiXe") // Specify the column name
    private String maTaiXe; // Change to lowercase

    @Column(name = "BienSoXe") // Specify the column name
    private String bienSoXe; // Change to lowercase

    private String ngayGioDangNhap; // Ensure this is ZonedDateTime
    private String ngayGioDangXuat;
    private String toaDoDangNhap;
    private String toaDoDangXuat;
}

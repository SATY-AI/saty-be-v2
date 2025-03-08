package com.ims.IMS.model.SatyJourney;


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
@Table(name = "saty_du_lieu") // Ensure the table name matches the database
public class SatyJourneyData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String customID;
    private String phoneNumber; // Change to phone_number in SQL queries
    private Float kinhDo; // Change to kinh_do in SQL queries
    private Float viDo; // Change to vi_do in SQL queries
    private Float tocDo; // Change to toc_do in SQL queries
    private Integer acc; // Change to acc in SQL queries
    private Integer ex1; // Change to ex1 in SQL queries
    private Integer ex2; // Change to ex2 in SQL queries
    private Integer mucNhienLieu; // Change to muc_nhien_lieu in SQL queries
    private Float nhietDo; // Change to nhiet_do in SQL queries
    private Float doAm; // Change to do_am in SQL queries
    private Integer rssi; // Change to rssi in SQL queries
    private String rtcServer; // Change to rtc_server in SQL queries
    private String rtcBoard;
    private String maTaiXe; // Change to ma_tai_xe in SQL queries
    private Integer trangThaiTaiXe; // Change to trang_thai_tai_xe in SQL queries
    private String sim; // Change to sim in SQL queries
    private String sdCard; // Change to sd_card in SQL queries
}

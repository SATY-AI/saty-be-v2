package com.ims.IMS.model.fertilizerProduct;


import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fertilizer_usage_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertilizerUsageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saty_journey_data_id") // Ensure the join column name matches your database schema
    private SatyJourneyData satyJourneyData; // Liên kết với bảng SatyJourneyData

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fertilizer_id", referencedColumnName = "id")
    private Fertilizer fertilizer; // Liên kết với bảng Fertilizer

    @Column(name = "amount_used")
    private double amountUsed; // Lượng phân bón đã sử dụng

    @Column(name = "date_used")
    private String dateUsed; // Ngày sử dụng phân bón
}


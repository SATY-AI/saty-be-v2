package com.ims.IMS.model.progress_plant;

import com.ims.IMS.model.SatyJourney.SatyDevice;
import com.ims.IMS.model.fertilizerProduct.FertilizerUsageLog;
import com.ims.IMS.model.imsprocessing.Device;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "progress_plant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressPlant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "plant_name")
    private String plantName;

    @Column(name = "plant_type")
    private String plantType;

    @Column(name = "height")
    private double height;             // Relation to Plant

    @Column(name = "planting_date")
    private LocalDateTime plantingDate;   // Date of planting

    @Column(name = "growth_stage")
    private String growthStage;       // Growth stage (e.g., Seedling, Vegetative)

    @Column(name = "is_perennial")
    private Boolean isPerennial;      // Whether the plant is perennial or not

    @ManyToOne(cascade = CascadeType.ALL) // Adjust cascade options based on your requirements
    @JoinColumn(name = "device_id")
    private SatyDevice device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fertilizer_usage_id")
    private FertilizerUsageLog fertilizerUsageLog;
}

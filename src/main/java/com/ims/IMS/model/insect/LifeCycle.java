package com.ims.IMS.model.insect;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "life_cycle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LifeCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "insect_id", nullable = false)
//    private Insect insect;

    @Column(name = "stage_name", nullable = false)
    private String stageName;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "description")
    private String description;
}

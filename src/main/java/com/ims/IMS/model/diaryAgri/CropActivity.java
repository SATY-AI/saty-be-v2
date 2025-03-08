package com.ims.IMS.model.diaryAgri;


import com.ims.IMS.utils.MapToJsonbConverter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "cropactivities_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cropactivities_test_id_seq")
    @SequenceGenerator(name = "cropactivities_test_id_seq", sequenceName = "cropactivities_test_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "_id")
    private String idField;

    @Column(name = "job")
    private String job;

    @Column(name = "date")
    private String date;

    @Column(name = "type")
    private String type;

    @Column(name = "metadata") // Specify the column type
    private String metadata; // Change to Map<String, Object>

    @Column(name = "crop_season_id")
    private String cropSeasonId;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;
}

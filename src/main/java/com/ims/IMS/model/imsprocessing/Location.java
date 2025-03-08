package com.ims.IMS.model.imsprocessing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ims.IMS.api.location.LocationRegisterRequest;
import com.ims.IMS.model.Enum.LocationProvince;
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

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private LocationProvince locationProvince;
    private String locationID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime updatedAt;
    private String description;
    private String urlLocationImage;

    public static Location fromAdminCreate(LocationRegisterRequest locationRegisterRequest) {
        return Location.builder()
                .locationID(locationRegisterRequest.locationID())
                .locationProvince(locationRegisterRequest.locationProvince())
                .description(locationRegisterRequest.description())
                .urlLocationImage(locationRegisterRequest.urlLocationImage())
                .createdAt(locationRegisterRequest.createdAt())
                .updatedAt(locationRegisterRequest.updatedAt())
                .build();
    }
}

package com.ims.IMS.model.imsprocessing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ims.IMS.api.device.DeviceRegisterRequest;
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
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String deviceID;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime created_at;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime updated_at;
    private String missionDescription;
    private String locationX;
    private String locationY;
    private String locationID;

    public static Device fromAdminCreate(DeviceRegisterRequest deviceRegisterRequest) {
        return Device.builder()
                .deviceID(deviceRegisterRequest.deviceID())
                .description(deviceRegisterRequest.description())
                .created_at(deviceRegisterRequest.created_at())
                .updated_at(deviceRegisterRequest.updated_at())
                .locationID(deviceRegisterRequest.locationID())
                .locationX(deviceRegisterRequest.locationX())
                .locationY(deviceRegisterRequest.locationY())
                .missionDescription(deviceRegisterRequest.missionDescription())
                .build();
    }
}

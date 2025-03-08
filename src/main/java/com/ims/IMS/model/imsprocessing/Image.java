package com.ims.IMS.model.imsprocessing;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ims.IMS.api.image.ImageRegisterRequest;
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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imageBucket;
    private String description;
    private String topic;
    private List<String> imageList;
    private String deviceID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime created_at;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime updated_at;

    public static Image fromAdminCreate(ImageRegisterRequest imageRegisterRequest) {
        return Image.builder()
                .imageBucket(imageRegisterRequest.imageBucket())
                .description(imageRegisterRequest.description())
                .topic(imageRegisterRequest.topic())
                .imageList(imageRegisterRequest.imageList())
                .deviceID(imageRegisterRequest.deviceID())
                .created_at(imageRegisterRequest.created_at())
                .updated_at(imageRegisterRequest.updated_at())
                .build();
    }
}

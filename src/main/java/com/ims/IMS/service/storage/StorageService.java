package com.ims.IMS.service.storage;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ims.IMS.api.image.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public FileUploadResponse uploadFile(MultipartFile file, String imageBucket, String topic, String deviceID, String imageName) {
        String filePath = bucketName + "/" + imageBucket + "/" + topic + "/" + deviceID;
        // Check if the file already exists
        if (s3Client.doesObjectExist(filePath, imageName)) {
            log.info("File with name {} already exists in path {}", imageName, filePath);
            return new FileUploadResponse(imageName, "File already exists in the bucket.");
        }
        // File doesn't exist, proceed to upload
        File fileObj = convertMultiPartFileToFile(file);
        try {
            s3Client.putObject(new PutObjectRequest(filePath, imageName, fileObj));
            log.info("File {} uploaded to path {}", imageName, filePath);
            return new FileUploadResponse(imageName, "File uploaded successfully.");
        } finally {
            fileObj.delete(); // Clean up temporary file
        }
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}

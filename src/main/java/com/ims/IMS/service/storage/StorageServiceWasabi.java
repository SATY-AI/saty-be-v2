package com.ims.IMS.service.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ims.IMS.api.image.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class StorageServiceWasabi {

    private final String accessKey;
    private final String secretKey;
    private final String bucketName;
    private final String region;
    private final AmazonS3 s3Client;

    // Constructor to inject the Wasabi configuration properties
    public StorageServiceWasabi(
            @Value("${wasabi.accessKey}") String accessKey,
            @Value("${wasabi.secretKey}") String secretKey,
            @Value("${wasabi.bucketName}") String bucketName,
            @Value("${wasabi.region}") String region) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.region = region;

        // Initialize S3 client with Wasabi endpoint
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(new AmazonS3ClientBuilder.EndpointConfiguration(
                        "https://s3." + region + ".wasabisys.com", region))  // Use the dynamic region in the endpoint
                .build();
    }

    public FileUploadResponse uploadFile(MultipartFile file, String imageBucket, String topic, String deviceID, String imageName) {
        String filePath = bucketName + "/" + imageBucket + "/" + topic + "/" + deviceID;

        try {
            // Check if the file already exists
            if (s3Client.doesObjectExist(filePath, imageName)) {
                return new FileUploadResponse(imageName, "File already exists in the bucket.");
            }

            // Convert and upload the file
            File fileObj = convertMultiPartFileToFile(file);
            s3Client.putObject(new PutObjectRequest(filePath, imageName, fileObj));
            return new FileUploadResponse(imageName, "File uploaded successfully.");
        } catch (Exception e) {
            log.error("Error uploading file to Wasabi: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public byte[] downloadFile(String fileName) {
        try {
            S3Object s3Object = s3Client.getObject(bucketName, fileName);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("Error downloading file from Wasabi: {}", e.getMessage());
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public String deleteFile(String fileName) {
        try {
            s3Client.deleteObject(bucketName, fileName);
            return fileName + " removed ...";
        } catch (Exception e) {
            log.error("Error deleting file from Wasabi: {}", e.getMessage());
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting MultipartFile to file: {}", e.getMessage());
            throw new RuntimeException("Failed to convert file", e);
        }
        return convertedFile;
    }
}

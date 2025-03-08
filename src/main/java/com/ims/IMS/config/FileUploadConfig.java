package com.ims.IMS.config;


import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // Maximum file size
        DataSize maxFileSize = DataSize.ofMegabytes(10);
        factory.setMaxFileSize(maxFileSize);

        // Maximum request size
        DataSize maxRequestSize = DataSize.ofMegabytes(10);
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }



}

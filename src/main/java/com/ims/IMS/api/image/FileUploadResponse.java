package com.ims.IMS.api.image;

public class FileUploadResponse {
    private String fileName;
    private String message;

    // Constructor
    public FileUploadResponse(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FileUploadResponse{" +
                "fileName='" + fileName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

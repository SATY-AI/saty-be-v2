package com.ims.IMS.exception;

public class WarehouseProcessingException extends RuntimeException {
    public WarehouseProcessingException(String message) {
        super(message);
    }

    public WarehouseProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

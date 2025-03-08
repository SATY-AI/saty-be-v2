package com.ims.IMS.exception;

public class UniqueCodeExistsException extends RuntimeException {
    public UniqueCodeExistsException(String message) {
        super(message);
    }
}
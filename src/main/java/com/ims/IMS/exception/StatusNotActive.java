package com.ims.IMS.exception;

public class StatusNotActive extends RuntimeException {
    public StatusNotActive(String message) {
        super(message);
    }
}
package com.ims.IMS.exception;

public class HomeUserAlreadyExistsException extends RuntimeException {
    public HomeUserAlreadyExistsException(String message) {
        super(message);
    }
}
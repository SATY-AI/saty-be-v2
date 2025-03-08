package com.ims.IMS.exception;

public class LoginWrongRole extends RuntimeException {
    public LoginWrongRole(String message) {
        super(message);
    }
}
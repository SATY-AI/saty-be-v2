package com.ims.IMS.lib.exception;


import com.ims.IMS.lib.api.FieldError;

import java.util.List;

public class ClientSideException extends RuntimeException {
    private final String code;
    private transient Object payload;
    private transient List<FieldError> fieldErrors;

    public ClientSideException(String code, String message, Exception exception) {
        super(message, exception);
        this.code = code;
    }

    public ClientSideException(String code, String message, Exception exception, Object payload) {
        super(message, exception);
        this.code = code;
        this.payload = payload;
    }

    public ClientSideException(String code, String message, Exception exception, List<FieldError> fieldErrors) {
        super(message, exception);
        this.code = code;
        this.fieldErrors = fieldErrors;
    }

    public ClientSideException(String code, String message, List<FieldError> fieldErrors) {
        super(message, (Throwable)null);
        this.code = code;
        this.fieldErrors = fieldErrors;
    }

    public ClientSideException(String code, String message) {
        super(message, (Throwable)null);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public Object getPayload() {
        return this.payload;
    }

    public List<FieldError> getFieldErrors() {
        return this.fieldErrors;
    }
}


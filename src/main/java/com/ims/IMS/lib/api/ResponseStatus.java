package com.ims.IMS.lib.api;


import java.util.Collections;
import java.util.List;

public record ResponseStatus(String code, String message, List<FieldError> errors) {
    public ResponseStatus(String code, String message) {
        this(code, message, Collections.emptyList());
    }

    public ResponseStatus(String code) {
        this(code, (String)null, (List)null);
    }

    public ResponseStatus(String code, String message, List<FieldError> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public List<FieldError> errors() {
        return this.errors;
    }
}

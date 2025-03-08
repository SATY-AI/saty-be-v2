package com.ims.IMS.lib.api;

public record FieldError(String field, String message) {
    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String field() {
        return this.field;
    }

    public String message() {
        return this.message;
    }
}

package com.ims.IMS.lib.api;

import org.slf4j.MDC;
import java.util.Collections;
import java.util.List;
import org.slf4j.MDC;
import java.util.Collections;
import java.util.List;

public record ResponseApi<T>(ResponseStatus status, T payload, ResponseMeta meta) {

    // Custom constructor for error cases with null payload
    public ResponseApi(ResponseStatus status, ResponseMeta meta) {
        this(status, null, meta);
    }

    // Static factory method for success responses
    public static <T> ResponseApi<T> success(ResponseStatus status, T payload) {
        ResponseMeta meta = ResponseMeta.fromRequestId(MDC.get("X-Request-ID"));
        return success(status, payload, meta);
    }

    public static <T> ResponseApi<T> success(T payload) {
        return success(new ResponseStatus("SUCCESS"), payload);
    }

    public static <T> ResponseApi<T> success(T payload, ResponseMeta meta) {
        return new ResponseApi<>(new ResponseStatus("SUCCESS"), payload, meta);
    }

    public static <T> ResponseApi<T> success(ResponseStatus status, T payload, ResponseMeta meta) {
        if (meta == null) {
            meta = ResponseMeta.fromRequestId(MDC.get("X-Request-ID"));
        }
        return new ResponseApi<>(status, payload, meta);
    }

    // Static factory method for error responses
    public static <T> ResponseApi<T> error(String errorCode, String errorMessage) {
        return error(errorCode, errorMessage, Collections.emptyList());
    }

    public static <T> ResponseApi<T> error(String errorCode, String errorMessage, List<FieldError> fieldErrors) {
        ResponseStatus responseStatus = new ResponseStatus(errorCode, errorMessage, fieldErrors);
        ResponseMeta meta = ResponseMeta.fromRequestId(MDC.get("X-Request-ID"));
        return new ResponseApi<>(responseStatus, meta);
    }

    public static <T> ResponseApi<T> error(String errorCode, String errorMessage, T payload) {
        ResponseStatus responseStatus = new ResponseStatus(errorCode, errorMessage, (List)null);
        ResponseMeta meta = ResponseMeta.fromRequestId(MDC.get("X-Request-ID"));
        return new ResponseApi<>(responseStatus, payload, meta);
    }
}

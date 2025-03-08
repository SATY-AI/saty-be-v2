package com.ims.IMS.lib.exception;


public class ServerSideException extends RuntimeException {
    private final String code;
    private transient Object payload;
    private transient String requestId;

    public ServerSideException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ServerSideException(String code, String message, Exception exception) {
        super(message, exception);
        this.code = code;
    }

    public ServerSideException(String code, String message, Exception exception, String requestId) {
        super(message, exception);
        this.code = code;
        this.requestId = requestId;
    }

    public ServerSideException(String code, String message, Exception exception, Object payloadParams) {
        super(message, exception);
        this.code = code;
        this.payload = payloadParams;
    }

    public String getCode() {
        return this.code;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public Object getPayload() {
        return this.payload;
    }
}

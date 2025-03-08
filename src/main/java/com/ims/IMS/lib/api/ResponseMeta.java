package com.ims.IMS.lib.api;

public record ResponseMeta(String requestId, String nextCursor) {
    public ResponseMeta(String requestId, String nextCursor) {
        this.requestId = requestId;
        this.nextCursor = nextCursor;
    }

    public static ResponseMeta fromRequestId(String requestId) {
        return new ResponseMeta(requestId, (String)null);
    }

    public String requestId() {
        return this.requestId;
    }

    public String nextCursor() {
        return this.nextCursor;
    }
}

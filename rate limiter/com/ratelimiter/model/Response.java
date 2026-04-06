package com.ratelimiter.model;

public class Response {

    private String clientId;
    private String body;
    private int statusCode;

    public Response(String clientId, String body, int statusCode) {
        this.clientId = clientId;
        this.body = body;
        this.statusCode = statusCode;
    }

    public String getClientId() {
        return clientId;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

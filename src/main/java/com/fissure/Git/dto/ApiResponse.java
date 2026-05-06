package com.fissure.Git.dto;

import java.time.Instant;
import java.util.Map;

public class ApiResponse {
    public boolean success;
    public String message;
    public String errorCode;
    public String timestamp;
    public Map<String, Object> data;

    public static ApiResponse ok(String message) {
        ApiResponse response = new ApiResponse();
        response.success = true;
        response.message = message;
        response.timestamp = Instant.now().toString();
        return response;
    }

    public static ApiResponse ok(String message, Map<String, Object> data) {
        ApiResponse response = ok(message);
        response.data = data;
        return response;
    }

    public static ApiResponse error(String message, String errorCode) {
        ApiResponse response = new ApiResponse();
        response.success = false;
        response.message = message;
        response.errorCode = errorCode;
        response.timestamp = Instant.now().toString();
        return response;
    }
}

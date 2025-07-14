package com.paybook.application.dto.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private int status;
    private String timestamp;
    private String data;

    public ErrorResponse(String message, int status, String timestamp) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }
}

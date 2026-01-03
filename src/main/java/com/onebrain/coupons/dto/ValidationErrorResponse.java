package com.onebrain.coupons.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationErrorResponse {

    private String error;
    private String message;
    private Map<String, String> validationErrors;
    private LocalDateTime timestamp;
    private int status;

    public ValidationErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ValidationErrorResponse(String error, String message, Map<String, String> validationErrors, int status) {
        this();
        this.error = error;
        this.message = message;
        this.validationErrors = validationErrors;
        this.status = status;
    }

}
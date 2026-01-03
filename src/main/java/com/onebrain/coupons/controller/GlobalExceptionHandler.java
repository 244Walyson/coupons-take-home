package com.onebrain.coupons.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.onebrain.coupons.dto.ErrorResponse;
import com.onebrain.coupons.dto.ValidationErrorResponse;
import com.onebrain.coupons.exception.CouponAlreadyDeletedException;
import com.onebrain.coupons.exception.CouponCodeAlreadyExistsException;
import com.onebrain.coupons.exception.CouponNotFoundException;
import com.onebrain.coupons.exception.InvalidCouponDataException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                "VALIDATION_ERROR",
                "Invalid input data provided",
                validationErrors,
                HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCouponNotFoundException(
            CouponNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "COUPON_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidCouponDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCouponDataException(
            InvalidCouponDataException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_COUPON_DATA",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CouponAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleCouponAlreadyDeletedException(
            CouponAlreadyDeletedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "COUPON_ALREADY_DELETED",
                ex.getMessage(),
                HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CouponCodeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCouponCodeAlreadyExistsException(
            CouponCodeAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "COUPON_CODE_ALREADY_EXISTS",
                ex.getMessage(),
                HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex) {

        // Log the actual exception for debugging
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
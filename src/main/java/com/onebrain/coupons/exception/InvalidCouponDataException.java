package com.onebrain.coupons.exception;

public class InvalidCouponDataException extends RuntimeException {

    public InvalidCouponDataException(String message) {
        super(message);
    }

    public InvalidCouponDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
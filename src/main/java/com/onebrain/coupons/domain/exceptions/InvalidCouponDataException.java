package com.onebrain.coupons.domain.exceptions;

public class InvalidCouponDataException extends RuntimeException {

    public InvalidCouponDataException(String message) {
        super(message);
    }

    public InvalidCouponDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.onebrain.coupons.exception;

public class CouponAlreadyDeletedException extends RuntimeException {

    public CouponAlreadyDeletedException(String message) {
        super(message);
    }

    public CouponAlreadyDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
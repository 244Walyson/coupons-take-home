package com.onebrain.coupons.domain.exceptions;

public class CouponAlreadyDeletedException extends RuntimeException {

    public CouponAlreadyDeletedException(String message) {
        super(message);
    }

    public CouponAlreadyDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
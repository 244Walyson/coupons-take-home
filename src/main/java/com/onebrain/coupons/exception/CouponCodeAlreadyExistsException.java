package com.onebrain.coupons.exception;

public class CouponCodeAlreadyExistsException extends RuntimeException {
    public CouponCodeAlreadyExistsException(String message) {
        super(message);
    }
}

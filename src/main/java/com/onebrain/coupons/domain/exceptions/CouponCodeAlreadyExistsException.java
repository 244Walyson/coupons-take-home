package com.onebrain.coupons.domain.exceptions;

public class CouponCodeAlreadyExistsException extends RuntimeException {
    public CouponCodeAlreadyExistsException(String message) {
        super(message);
    }
}

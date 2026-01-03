package com.onebrain.coupons.application.usecases;

import java.util.UUID;

import com.onebrain.coupons.domain.exceptions.CouponNotFoundException;
import com.onebrain.coupons.domain.interfaces.CouponRepository;
import com.onebrain.coupons.domain.model.Coupon;

public class GetCouponByIdUseCase {

    private final CouponRepository couponRepository;

    public GetCouponByIdUseCase(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon execute(UUID id) {
        return couponRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
    }
}
package com.onebrain.coupons.application.usecases;

import java.util.UUID;

import com.onebrain.coupons.domain.exceptions.CouponNotFoundException;
import com.onebrain.coupons.domain.interfaces.CouponRepository;
import com.onebrain.coupons.domain.model.Coupon;

public class DeleteCouponUseCase {

    private final CouponRepository couponRepository;

    public DeleteCouponUseCase(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void execute(UUID id) {
        Coupon coupon = couponRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));

        coupon.softDelete();
        couponRepository.save(coupon);
    }
}
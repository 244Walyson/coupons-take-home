package com.onebrain.coupons.application.usecases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.onebrain.coupons.domain.interfaces.CouponRepository;
import com.onebrain.coupons.domain.model.Coupon;

public class CreateCouponUseCase {

    private final CouponRepository couponRepository;

    public CreateCouponUseCase(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon execute(String code, String description, BigDecimal discountValue,
            LocalDateTime expirationDate, Boolean published) {

        Coupon coupon = new Coupon(code, description, discountValue, expirationDate, published);

        if (couponRepository.existsByCodeAndStatusNot(coupon.getCode(),
                com.onebrain.coupons.domain.enums.CouponStatus.DELETED)) {
            throw new com.onebrain.coupons.domain.exceptions.CouponCodeAlreadyExistsException(
                    "Coupon with code " + coupon.getCode() + " already exists and is active");
        }

        return couponRepository.save(coupon);
    }
}
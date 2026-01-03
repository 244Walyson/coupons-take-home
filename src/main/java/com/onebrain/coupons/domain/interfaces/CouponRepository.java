package com.onebrain.coupons.domain.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.onebrain.coupons.domain.model.Coupon;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findByIdAndNotDeleted(UUID id);

    boolean existsByCode(String code);

    boolean existsByCodeAndStatusNot(String code, com.onebrain.coupons.domain.enums.CouponStatus status);
}
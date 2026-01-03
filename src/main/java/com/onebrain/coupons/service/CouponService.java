package com.onebrain.coupons.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onebrain.coupons.entity.Coupon;
import com.onebrain.coupons.enums.CouponStatus;
import com.onebrain.coupons.exception.CouponCodeAlreadyExistsException;
import com.onebrain.coupons.exception.CouponNotFoundException;
import com.onebrain.coupons.repository.CouponRepository;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public Coupon createCoupon(String code, String description, BigDecimal discountValue,
            LocalDateTime expirationDate, Boolean published) {

        Coupon coupon = new Coupon(code, description, discountValue, expirationDate, published);

        if (couponRepository.existsByCodeAndStatusNot(coupon.getCode(), CouponStatus.DELETED)) {
            throw new CouponCodeAlreadyExistsException(
                    "Coupon with code " + coupon.getCode() + " already exists and is active");
        }

        return couponRepository.save(coupon);
    }

    @Transactional(readOnly = true)
    public Coupon getCouponById(UUID id) {
        return couponRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id " + id));
    }

    @Transactional
    public void deleteCoupon(UUID id) {
        Coupon coupon = getCouponById(id);
        coupon.softDelete();
        couponRepository.save(coupon);
    }
}

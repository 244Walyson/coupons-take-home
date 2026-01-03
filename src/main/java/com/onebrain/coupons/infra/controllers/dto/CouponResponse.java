package com.onebrain.coupons.infra.controllers.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.onebrain.coupons.domain.enums.CouponStatus;
import com.onebrain.coupons.domain.model.Coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CouponResponse {

    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime expirationDate;
    private CouponStatus status;
    private Boolean published;
    private Boolean redeemed;

    public CouponResponse(Coupon coupon) {
        this.id = coupon.getId();
        this.code = coupon.getCode();
        this.description = coupon.getDescription();
        this.discountValue = coupon.getDiscountValue();
        this.expirationDate = coupon.getExpirationDate();
        this.status = coupon.getStatus();
        this.published = coupon.getPublished();
        this.redeemed = coupon.getRedeemed();
    }

}
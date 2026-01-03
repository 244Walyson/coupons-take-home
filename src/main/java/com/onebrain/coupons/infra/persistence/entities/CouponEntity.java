package com.onebrain.coupons.infra.persistence.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.onebrain.coupons.domain.enums.CouponStatus;
import com.onebrain.coupons.domain.model.Coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coupons")
public class CouponEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private Boolean published;

    @Column(nullable = false)
    private Boolean redeemed;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public CouponEntity() {
    }

    public CouponEntity(Coupon coupon) {
        this.id = coupon.getId();
        this.code = coupon.getCode();
        this.description = coupon.getDescription();
        this.discountValue = coupon.getDiscountValue();
        this.expirationDate = coupon.getExpirationDate();
        this.status = coupon.getStatus();
        this.published = coupon.getPublished();
        this.redeemed = coupon.getRedeemed();
        this.createdAt = coupon.getCreatedAt();
        this.updatedAt = coupon.getUpdatedAt();
    }

    public Coupon toDomain() {
        Coupon coupon = new Coupon();
        coupon.setId(this.id);
        coupon.setCode(this.code);
        coupon.setDescription(this.description);
        coupon.setDiscountValue(this.discountValue);
        coupon.setExpirationDate(this.expirationDate);
        coupon.setStatus(this.status);
        coupon.setPublished(this.published);
        coupon.setRedeemed(this.redeemed);
        coupon.setCreatedAt(this.createdAt);
        coupon.setUpdatedAt(this.updatedAt);
        return coupon;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
package com.onebrain.coupons.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.onebrain.coupons.enums.CouponStatus;
import com.onebrain.coupons.exception.CouponAlreadyDeletedException;
import com.onebrain.coupons.exception.InvalidCouponDataException;

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
public class Coupon {

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
    private CouponStatus status = CouponStatus.ACTIVE;

    @Column(nullable = false)
    private Boolean published = false;

    @Column(nullable = false)
    private Boolean redeemed = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    public Coupon() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Coupon(String code, String description, BigDecimal discountValue,
            LocalDateTime expirationDate, Boolean published) {
        this();
        this.code = sanitizeCode(code);
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published != null ? published : false;

        validateBusinessRules();
    }

    private String sanitizeCode(String code) {
        if (code == null)
            return null;

        String sanitized = code.replaceAll("[^a-zA-Z0-9]", "");

        return sanitized.toUpperCase();
    }

    private void validateBusinessRules() {
        if (code == null || code.trim().isEmpty()) {
            throw new InvalidCouponDataException("Code is required");
        }

        if (code.length() != 6) {
            throw new InvalidCouponDataException(
                    "Coupon code must be exactly 6 alphanumeric characters after cleaning");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new InvalidCouponDataException("Description is required");
        }

        if (discountValue == null) {
            throw new InvalidCouponDataException("Discount value is required");
        }

        if (discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            throw new InvalidCouponDataException("Discount value must be at least 0.5");
        }

        if (expirationDate == null) {
            throw new InvalidCouponDataException("Expiration date is required");
        }

        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw new InvalidCouponDataException("Expiration date cannot be in the past");
        }
    }

    public void softDelete() {
        if (this.status == CouponStatus.DELETED) {
            throw new CouponAlreadyDeletedException("Coupon is already deleted");
        }
        this.status = CouponStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

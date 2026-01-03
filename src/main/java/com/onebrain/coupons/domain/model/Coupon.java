package com.onebrain.coupons.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.onebrain.coupons.domain.enums.CouponStatus;
import com.onebrain.coupons.domain.exceptions.CouponAlreadyDeletedException;
import com.onebrain.coupons.domain.exceptions.InvalidCouponDataException;

public class Coupon {

    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime expirationDate;
    private CouponStatus status = CouponStatus.ACTIVE;
    private Boolean published = false;
    private Boolean redeemed = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(Boolean redeemed) {
        this.redeemed = redeemed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
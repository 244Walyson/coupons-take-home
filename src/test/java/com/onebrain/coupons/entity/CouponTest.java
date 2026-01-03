package com.onebrain.coupons.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.onebrain.coupons.enums.CouponStatus;
import com.onebrain.coupons.exception.CouponAlreadyDeletedException;

class CouponTest {

    @Test
    void shouldSanitizeCodeRemovingSpecialCharacters() {
        // Given
        String codeWithSpecialChars = "ABC-123";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon(codeWithSpecialChars, "Test Description",
                new BigDecimal("1.0"), futureDate, false);

        // Then
        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    void shouldRemoveMultipleSpecialCharacters() {
        // Given
        String codeWithMultipleSpecialChars = "A!B@C#1$2%3";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon(codeWithMultipleSpecialChars, "Test Description",
                new BigDecimal("1.0"), futureDate, false);

        // Then
        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    void shouldThrowExceptionForCodeLongerThanSixCharacters() {
        // Given
        String longCode = "ABCDEFGHIJ";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When & Then
        assertThrows(com.onebrain.coupons.exception.InvalidCouponDataException.class, () -> {
            new Coupon(longCode, "Test Description",
                    new BigDecimal("1.0"), futureDate, false);
        });
    }

    @Test
    void shouldThrowExceptionForCodeShorterThanSixCharacters() {
        // Given
        String shortCode = "ABC";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When & Then
        assertThrows(com.onebrain.coupons.exception.InvalidCouponDataException.class, () -> {
            new Coupon(shortCode, "Test Description",
                    new BigDecimal("1.0"), futureDate, false);
        });
    }

    @Test
    void shouldThrowExceptionForVeryShortCode() {
        // Given
        String veryShortCode = "A";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When & Then
        assertThrows(com.onebrain.coupons.exception.InvalidCouponDataException.class, () -> {
            new Coupon(veryShortCode, "Test Description",
                    new BigDecimal("1.0"), futureDate, false);
        });
    }

    @Test
    void shouldConvertCodeToUpperCase() {
        // Given
        String lowerCaseCode = "abc123";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon(lowerCaseCode, "Test Description",
                new BigDecimal("1.0"), futureDate, false);

        // Then
        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    void shouldHandleMixedCaseCode() {
        // Given
        String mixedCaseCode = "aBc123";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon(mixedCaseCode, "Test Description",
                new BigDecimal("1.0"), futureDate, false);

        // Then
        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    void shouldCreateCouponWithDefaultValues() {
        // Given
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon("ABC123", "Test Description",
                new BigDecimal("1.0"), futureDate, null);

        // Then
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
        assertFalse(coupon.getPublished());
        assertFalse(coupon.getRedeemed());
        assertNotNull(coupon.getCreatedAt());
        assertNotNull(coupon.getUpdatedAt());
    }

    @Test
    void shouldCreateCouponWithPublishedTrue() {
        // Given
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon("ABC123", "Test Description",
                new BigDecimal("1.0"), futureDate, true);

        // Then
        assertTrue(coupon.getPublished());
    }

    @Test
    void shouldCreateCouponWithPublishedFalse() {
        // Given
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon("ABC123", "Test Description",
                new BigDecimal("1.0"), futureDate, false);

        // Then
        assertFalse(coupon.getPublished());
    }

    @Test
    void shouldSoftDeleteCoupon() {
        // Given
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        Coupon coupon = new Coupon("ABC123", "Test Description",
                new BigDecimal("1.0"), futureDate, false);

        // When
        coupon.softDelete();

        // Then
        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        assertNotNull(coupon.getDeletedAt());
    }

    @Test
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        // Given
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        Coupon coupon = new Coupon("ABC123", "Test Description",
                new BigDecimal("1.0"), futureDate, false);
        coupon.softDelete();

        // When & Then
        assertThrows(CouponAlreadyDeletedException.class, coupon::softDelete);
    }

    @Test
    void shouldPreserveOriginalExpirationDate() {
        // Given
        LocalDateTime specificDate = LocalDateTime.of(2026, 12, 31, 23, 59, 59); // Future date

        // When
        Coupon coupon = new Coupon("ABC123", "Test Description",
                new BigDecimal("1.0"), specificDate, false);

        // Then
        assertEquals(specificDate, coupon.getExpirationDate());
    }

    @Test
    void shouldPreserveOriginalDiscountValue() {
        // Given
        BigDecimal specificDiscount = new BigDecimal("99.99");
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon("ABC123", "Test Description",
                specificDiscount, futureDate, false);

        // Then
        assertEquals(specificDiscount, coupon.getDiscountValue());
    }

    @Test
    void shouldPreserveOriginalDescription() {
        // Given
        String longDescription = "This is a very long description that should be preserved exactly as provided without any modifications";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon("ABC123", longDescription,
                new BigDecimal("1.0"), futureDate, false);

        // Then
        assertEquals(longDescription, coupon.getDescription());
    }

    @Test
    void shouldThrowExceptionForEmptyCode() {
        // Given
        String emptyCode = "";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When & Then
        assertThrows(com.onebrain.coupons.exception.InvalidCouponDataException.class, () -> {
            new Coupon(emptyCode, "Test Description",
                    new BigDecimal("1.0"), futureDate, false);
        });
    }

    @Test
    void shouldThrowExceptionForCodeWithOnlySpecialCharacters() {
        // Given
        String specialCharsCode = "!@#$%^";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When & Then
        assertThrows(com.onebrain.coupons.exception.InvalidCouponDataException.class, () -> {
            new Coupon(specialCharsCode, "Test Description",
                    new BigDecimal("1.0"), futureDate, false);
        });
    }

    @Test
    void shouldSetTimestampsOnCreation() {
        // Given
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // When
        Coupon coupon = new Coupon("ABC123", "Test Description",
                new BigDecimal("1.0"), futureDate, false);
        LocalDateTime afterCreation = LocalDateTime.now().plusSeconds(1);

        // Then
        assertNotNull(coupon.getCreatedAt());
        assertNotNull(coupon.getUpdatedAt());
        assertTrue(coupon.getCreatedAt().isAfter(beforeCreation));
        assertTrue(coupon.getCreatedAt().isBefore(afterCreation));
        assertTrue(coupon.getUpdatedAt().isAfter(beforeCreation));
        assertTrue(coupon.getUpdatedAt().isBefore(afterCreation));
    }
}
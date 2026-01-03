package com.onebrain.coupons.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.onebrain.coupons.application.usecases.CreateCouponUseCase;
import com.onebrain.coupons.domain.enums.CouponStatus;
import com.onebrain.coupons.domain.exceptions.InvalidCouponDataException;
import com.onebrain.coupons.domain.interfaces.CouponRepository;
import com.onebrain.coupons.domain.model.Coupon;

class CreateCouponUseCaseTest {

    @Mock
    private CouponRepository couponRepository;

    private CreateCouponUseCase createCouponUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createCouponUseCase = new CreateCouponUseCase(couponRepository);
    }

    @Test
    void shouldCreateCouponSuccessfully() {
        // Given
        String code = "ABC123";
        String description = "Test coupon";
        BigDecimal discountValue = new BigDecimal("1.0");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        Boolean published = false;

        Coupon expectedCoupon = new Coupon(code, description, discountValue, expirationDate, published);
        when(couponRepository.save(any(Coupon.class))).thenReturn(expectedCoupon);

        // When
        Coupon result = createCouponUseCase.execute(code, description, discountValue, expirationDate, published);

        // Then
        assertNotNull(result);
        assertEquals(code, result.getCode());
        assertEquals(description, result.getDescription());
        assertEquals(discountValue, result.getDiscountValue());
        assertEquals(expirationDate, result.getExpirationDate());
        assertEquals(published, result.getPublished());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());
        assertFalse(result.getRedeemed());
    }

    @Test
    void shouldCreatePublishedCoupon() {
        // Given
        String code = "PUB123";
        String description = "Published coupon";
        BigDecimal discountValue = new BigDecimal("25.00");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);
        Boolean published = true;

        Coupon expectedCoupon = new Coupon(code, description, discountValue, expirationDate, published);
        when(couponRepository.save(any(Coupon.class))).thenReturn(expectedCoupon);

        // When
        Coupon result = createCouponUseCase.execute(code, description, discountValue, expirationDate, published);

        // Then
        assertNotNull(result);
        assertTrue(result.getPublished());
        assertEquals("PUB123", result.getCode());
    }

    @Test
    void shouldCreateCouponWithNullPublishedDefaultingToFalse() {
        // Given
        String code = "DEF123";
        String description = "Default published coupon";
        BigDecimal discountValue = new BigDecimal("5.00");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(7);
        Boolean published = null;

        Coupon expectedCoupon = new Coupon(code, description, discountValue, expirationDate, published);
        when(couponRepository.save(any(Coupon.class))).thenReturn(expectedCoupon);

        // When
        Coupon result = createCouponUseCase.execute(code, description, discountValue, expirationDate, published);

        // Then
        assertNotNull(result);
        assertFalse(result.getPublished());
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsTooLow() {
        // Given
        String code = "ABC123";
        String description = "Test coupon";
        BigDecimal discountValue = new BigDecimal("0.3");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        Boolean published = false;

        // When & Then
        assertThrows(InvalidCouponDataException.class, () -> {
            createCouponUseCase.execute(code, description, discountValue, expirationDate, published);
        });
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsZero() {
        // Given
        String code = "ABC123";
        String description = "Test coupon";
        BigDecimal discountValue = BigDecimal.ZERO;
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        Boolean published = false;

        // When & Then
        assertThrows(InvalidCouponDataException.class, () -> {
            createCouponUseCase.execute(code, description, discountValue, expirationDate, published);
        });
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsNegative() {
        // Given
        String code = "ABC123";
        String description = "Test coupon";
        BigDecimal discountValue = new BigDecimal("-1.0");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        Boolean published = false;

        // When & Then
        assertThrows(InvalidCouponDataException.class, () -> {
            createCouponUseCase.execute(code, description, discountValue, expirationDate, published);
        });
    }

    @Test
    void shouldThrowExceptionWhenExpirationDateIsInPast() {
        // Given
        String code = "ABC123";
        String description = "Test coupon";
        BigDecimal discountValue = new BigDecimal("1.0");
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(1); // Past date
        Boolean published = false;

        // When & Then
        assertThrows(InvalidCouponDataException.class, () -> {
            createCouponUseCase.execute(code, description, discountValue, expirationDate, published);
        });
    }

    @Test
    void shouldThrowExceptionWhenExpirationDateIsNow() {
        // Given
        String code = "ABC123";
        String description = "Test coupon";
        BigDecimal discountValue = new BigDecimal("1.0");
        LocalDateTime expirationDate = LocalDateTime.now();
        Boolean published = false;

        // When & Then
        assertThrows(InvalidCouponDataException.class, () -> {
            createCouponUseCase.execute(code, description, discountValue, expirationDate, published);
        });
    }

    @Test
    void shouldSanitizeCodeWithSpecialCharacters() {
        // Given
        String code = "ABC-123";
        String description = "Test coupon";
        BigDecimal discountValue = new BigDecimal("1.0");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        Boolean published = false;

        Coupon expectedCoupon = new Coupon(code, description, discountValue, expirationDate, published);
        when(couponRepository.save(any(Coupon.class))).thenReturn(expectedCoupon);

        // When
        Coupon result = createCouponUseCase.execute(code, description, discountValue, expirationDate, published);

        // Then
        assertEquals("ABC123", result.getCode());
    }

    @Test
    void shouldCreateCouponWithMaximumDiscountValue() {
        // Given
        String code = "MAX123";
        String description = "Maximum discount coupon";
        BigDecimal discountValue = new BigDecimal("999.99");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(365);
        Boolean published = true;

        Coupon expectedCoupon = new Coupon(code, description, discountValue, expirationDate, published);
        when(couponRepository.save(any(Coupon.class))).thenReturn(expectedCoupon);

        // When
        Coupon result = createCouponUseCase.execute(code, description, discountValue, expirationDate, published);

        // Then
        assertNotNull(result);
        assertEquals(discountValue, result.getDiscountValue());
    }

    @Test
    void shouldCreateCouponWithLongDescription() {
        // Given
        String code = "LONG12";
        String description = "This is a very long description for a coupon that should be accepted by the system without any issues";
        BigDecimal discountValue = new BigDecimal("10.00");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);
        Boolean published = false;

        Coupon expectedCoupon = new Coupon(code, description, discountValue, expirationDate, published);
        when(couponRepository.save(any(Coupon.class))).thenReturn(expectedCoupon);

        // When
        Coupon result = createCouponUseCase.execute(code, description, discountValue, expirationDate, published);

        // Then
        assertNotNull(result);
        assertEquals(description, result.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenCouponCodeAlreadyExistsAndActive() {
        // Given
        String code = "ABC123";
        String description = "Duplicate coupon";
        BigDecimal discountValue = new BigDecimal("10.00");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(10);
        Boolean published = true;

        when(couponRepository.existsByCodeAndStatusNot("ABC123", CouponStatus.DELETED)).thenReturn(true);

        // When & Then
        assertThrows(com.onebrain.coupons.domain.exceptions.CouponCodeAlreadyExistsException.class, () -> {
            createCouponUseCase.execute(code, description, discountValue, expirationDate, published);
        });
    }

    @Test
    void shouldCreateCouponWhenCouponCodeExistsButDeleted() {
        // Given
        String code = "ABC123";
        String description = "Reused coupon";
        BigDecimal discountValue = new BigDecimal("10.00");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(10);
        Boolean published = true;

        Coupon expectedCoupon = new Coupon(code, description, discountValue, expirationDate, published);

        when(couponRepository.existsByCodeAndStatusNot("ABC123", CouponStatus.DELETED)).thenReturn(false);
        when(couponRepository.save(any(Coupon.class))).thenReturn(expectedCoupon);

        // When
        Coupon result = createCouponUseCase.execute(code, description, discountValue, expirationDate, published);

        // Then
        assertNotNull(result);
        assertEquals("ABC123", result.getCode());
    }
}
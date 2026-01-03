package com.onebrain.coupons.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.onebrain.coupons.application.usecases.GetCouponByIdUseCase;
import com.onebrain.coupons.domain.enums.CouponStatus;
import com.onebrain.coupons.domain.exceptions.CouponNotFoundException;
import com.onebrain.coupons.domain.interfaces.CouponRepository;
import com.onebrain.coupons.domain.model.Coupon;

class GetCouponByIdUseCaseTest {

    @Mock
    private CouponRepository couponRepository;

    private GetCouponByIdUseCase getCouponByIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getCouponByIdUseCase = new GetCouponByIdUseCase(couponRepository);
    }

    @Test
    void shouldReturnCouponWhenFound() {
        // Given
        UUID couponId = UUID.randomUUID();
        Coupon expectedCoupon = new Coupon("ABC123", "Test coupon",
                new BigDecimal("10.50"),
                LocalDateTime.now().plusDays(30), true);

        when(couponRepository.findByIdAndNotDeleted(couponId)).thenReturn(Optional.of(expectedCoupon));

        // When
        Coupon result = getCouponByIdUseCase.execute(couponId);

        // Then
        assertNotNull(result);
        assertEquals("ABC123", result.getCode());
        assertEquals("Test coupon", result.getDescription());
        assertEquals(new BigDecimal("10.50"), result.getDiscountValue());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCouponNotFound() {
        // Given
        UUID couponId = UUID.randomUUID();
        when(couponRepository.findByIdAndNotDeleted(couponId)).thenReturn(Optional.empty());

        // When & Then
        CouponNotFoundException exception = assertThrows(CouponNotFoundException.class, () -> {
            getCouponByIdUseCase.execute(couponId);
        });

        assertEquals("Coupon not found with id: " + couponId, exception.getMessage());
    }

    @Test
    void shouldReturnUnpublishedCoupon() {
        // Given
        UUID couponId = UUID.randomUUID();
        Coupon unpublishedCoupon = new Coupon("UNP123", "Unpublished coupon",
                new BigDecimal("15.00"),
                LocalDateTime.now().plusDays(10), false);

        when(couponRepository.findByIdAndNotDeleted(couponId)).thenReturn(Optional.of(unpublishedCoupon));

        // When
        Coupon result = getCouponByIdUseCase.execute(couponId);

        // Then
        assertNotNull(result);
        assertEquals("UNP123", result.getCode());
        assertEquals(false, result.getPublished());
    }
}
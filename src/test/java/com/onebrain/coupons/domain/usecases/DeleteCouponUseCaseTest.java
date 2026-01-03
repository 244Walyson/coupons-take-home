package com.onebrain.coupons.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.onebrain.coupons.application.usecases.DeleteCouponUseCase;
import com.onebrain.coupons.domain.enums.CouponStatus;
import com.onebrain.coupons.domain.exceptions.CouponAlreadyDeletedException;
import com.onebrain.coupons.domain.exceptions.CouponNotFoundException;
import com.onebrain.coupons.domain.interfaces.CouponRepository;
import com.onebrain.coupons.domain.model.Coupon;

class DeleteCouponUseCaseTest {

    @Mock
    private CouponRepository couponRepository;

    private DeleteCouponUseCase deleteCouponUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteCouponUseCase = new DeleteCouponUseCase(couponRepository);
    }

    @Test
    void shouldDeleteCouponSuccessfully() {
        // Given
        UUID couponId = UUID.randomUUID();
        Coupon coupon = new Coupon("ABC123", "Test coupon",
                new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(1), false);

        when(couponRepository.findByIdAndNotDeleted(couponId)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        // When
        deleteCouponUseCase.execute(couponId);

        // Then
        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        verify(couponRepository).save(coupon);
    }

    @Test
    void shouldThrowExceptionWhenCouponNotFound() {
        // Given
        UUID couponId = UUID.randomUUID();
        when(couponRepository.findByIdAndNotDeleted(couponId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CouponNotFoundException.class, () -> {
            deleteCouponUseCase.execute(couponId);
        });
    }

    @Test
    void shouldThrowExceptionWhenCouponAlreadyDeleted() {
        // Given
        UUID couponId = UUID.randomUUID();
        Coupon coupon = new Coupon("ABC123", "Test coupon",
                new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(1), false);
        coupon.softDelete();

        when(couponRepository.findByIdAndNotDeleted(couponId)).thenReturn(Optional.of(coupon));

        // When & Then
        assertThrows(CouponAlreadyDeletedException.class, () -> {
            deleteCouponUseCase.execute(couponId);
        });
    }
}
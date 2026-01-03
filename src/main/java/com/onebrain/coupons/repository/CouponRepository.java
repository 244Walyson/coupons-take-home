package com.onebrain.coupons.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onebrain.coupons.entity.Coupon;
import com.onebrain.coupons.enums.CouponStatus;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    @Query("SELECT c FROM Coupon c WHERE c.id = :id AND c.status <> 'DELETED'")
    Optional<Coupon> findByIdAndNotDeleted(@Param("id") UUID id);

    boolean existsByCode(String code);

    boolean existsByCodeAndStatusNot(String code, CouponStatus status);
}

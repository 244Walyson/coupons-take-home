package com.onebrain.coupons.infra.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.onebrain.coupons.domain.enums.CouponStatus;
import com.onebrain.coupons.infra.persistence.entities.CouponEntity;

@Repository
public interface CouponJpaRepository extends JpaRepository<CouponEntity, UUID> {

    @Query("SELECT c FROM CouponEntity c WHERE c.id = :id AND c.status != 'DELETED'")
    Optional<CouponEntity> findByIdAndNotDeleted(UUID id);

    Optional<CouponEntity> findByCodeAndStatus(String code, CouponStatus status);

    boolean existsByCode(String code);

    boolean existsByCodeAndStatusNot(String code, CouponStatus status);
}
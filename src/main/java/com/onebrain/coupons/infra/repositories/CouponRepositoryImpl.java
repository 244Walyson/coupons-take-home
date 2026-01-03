package com.onebrain.coupons.infra.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.onebrain.coupons.domain.interfaces.CouponRepository;
import com.onebrain.coupons.domain.model.Coupon;
import com.onebrain.coupons.infra.persistence.entities.CouponEntity;

@Component
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository jpaRepository;

    @Autowired
    public CouponRepositoryImpl(CouponJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity entity = new CouponEntity(coupon);
        CouponEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Coupon> findByIdAndNotDeleted(UUID id) {
        return jpaRepository.findByIdAndNotDeleted(id)
                .map(CouponEntity::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public boolean existsByCodeAndStatusNot(String code, com.onebrain.coupons.domain.enums.CouponStatus status) {
        return jpaRepository.existsByCodeAndStatusNot(code, status);
    }
}
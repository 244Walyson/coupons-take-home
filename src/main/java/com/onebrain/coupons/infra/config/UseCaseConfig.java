package com.onebrain.coupons.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onebrain.coupons.application.usecases.CreateCouponUseCase;
import com.onebrain.coupons.application.usecases.DeleteCouponUseCase;
import com.onebrain.coupons.application.usecases.GetCouponByIdUseCase;
import com.onebrain.coupons.domain.interfaces.CouponRepository;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateCouponUseCase createCouponUseCase(CouponRepository couponRepository) {
        return new CreateCouponUseCase(couponRepository);
    }

    @Bean
    public GetCouponByIdUseCase getCouponByIdUseCase(CouponRepository couponRepository) {
        return new GetCouponByIdUseCase(couponRepository);
    }

    @Bean
    public DeleteCouponUseCase deleteCouponUseCase(CouponRepository couponRepository) {
        return new DeleteCouponUseCase(couponRepository);
    }
}
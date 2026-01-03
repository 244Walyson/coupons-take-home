package com.onebrain.coupons.infra.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onebrain.coupons.application.usecases.CreateCouponUseCase;
import com.onebrain.coupons.application.usecases.DeleteCouponUseCase;
import com.onebrain.coupons.application.usecases.GetCouponByIdUseCase;
import com.onebrain.coupons.domain.model.Coupon;
import com.onebrain.coupons.infra.controllers.dto.CouponResponse;
import com.onebrain.coupons.infra.controllers.dto.CreateCouponRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/coupon")
@Tag(name = "Coupon", description = "Coupon management API")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final GetCouponByIdUseCase getCouponByIdUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;

    public CouponController(CreateCouponUseCase createCouponUseCase,
            GetCouponByIdUseCase getCouponByIdUseCase,
            DeleteCouponUseCase deleteCouponUseCase) {
        this.createCouponUseCase = createCouponUseCase;
        this.getCouponByIdUseCase = getCouponByIdUseCase;
        this.deleteCouponUseCase = deleteCouponUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new coupon", description = "Creates a new coupon with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Coupon created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        Coupon coupon = createCouponUseCase.execute(
                request.getCode(),
                request.getDescription(),
                request.getDiscountValue(),
                request.getExpirationDate(),
                request.getPublished());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CouponResponse(coupon));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID", description = "Retrieves a coupon by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon found"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<CouponResponse> getCouponById(
            @Parameter(description = "Coupon ID", example = "df7ddff3-03f5-4362-9bf5-5a5a8ce47b93") @PathVariable UUID id) {
        Coupon coupon = getCouponByIdUseCase.execute(id);
        return ResponseEntity.ok(new CouponResponse(coupon));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete coupon", description = "Performs a soft delete on the specified coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Coupon deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found"),
            @ApiResponse(responseCode = "400", description = "Coupon already deleted")
    })
    public ResponseEntity<Void> deleteCoupon(
            @Parameter(description = "Coupon ID", example = "df7ddff3-03f5-4362-9bf5-5a5a8ce47b93") @PathVariable UUID id) {
        deleteCouponUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
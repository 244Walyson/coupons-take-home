package com.onebrain.coupons.controller;

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

import com.onebrain.coupons.dto.CouponResponse;
import com.onebrain.coupons.dto.CreateCouponRequest;
import com.onebrain.coupons.entity.Coupon;
import com.onebrain.coupons.service.CouponService;

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

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    @Operation(summary = "Create a new coupon", description = "Creates a new coupon with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Coupon created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        Coupon coupon = couponService.createCoupon(
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
        Coupon coupon = couponService.getCouponById(id);
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
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }
}
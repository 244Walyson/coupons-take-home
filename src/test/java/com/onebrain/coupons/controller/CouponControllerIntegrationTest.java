package com.onebrain.coupons.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebrain.coupons.dto.CreateCouponRequest;

@SpringBootTest
class CouponControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void createCoupon_ShouldReturnCreated_WhenDataIsValid() throws Exception {
        CreateCouponRequest request = new CreateCouponRequest(
                "TESTVA",
                "Integration Test Coupon",
                new BigDecimal("10.5"),
                LocalDateTime.now().plusDays(10),
                true);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("TESTVA"))
                .andExpect(jsonPath("$.description").value("Integration Test Coupon"))
                .andExpect(jsonPath("$.discountValue").value(10.5));
    }

    @Test
    void createCoupon_ShouldReturnCreated_WhenDataHasSpecialCharsAndResultsIn6Chars() throws Exception {
        CreateCouponRequest request = new CreateCouponRequest(
                "@A#B$C%D^E&F",
                "Special Chars Coupon Code",
                new BigDecimal("10.5"),
                LocalDateTime.now().plusDays(10),
                true);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABCDEF"));
    }

    @Test
    void createCoupon_ShouldReturnBadRequest_WhenDataIsInvalid() throws Exception {
        CreateCouponRequest request = new CreateCouponRequest(
                "",
                "",
                new BigDecimal("0.1"),
                LocalDateTime.now().minusDays(1),
                true);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCoupon_ShouldReturnBadRequest_WhenCodeIsTooShortAfterCleaning() throws Exception {

        CreateCouponRequest requestShort = new CreateCouponRequest(
                "ABC",
                "Short Code",
                new BigDecimal("10.5"),
                LocalDateTime.now().plusDays(10),
                true);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestShort)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCoupon_ShouldReturnBadRequest_WhenCodeIsTooLongAfterCleaning() throws Exception {

        CreateCouponRequest requestLong = new CreateCouponRequest(
                "ABCDEFG",
                "Long Code",
                new BigDecimal("10.5"),
                LocalDateTime.now().plusDays(10),
                true);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestLong)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCouponById_ShouldReturnCoupon_WhenExists() throws Exception {

        CreateCouponRequest request = new CreateCouponRequest(
                "GETTES",
                "Get Test Coupon",
                new BigDecimal("5.0"),
                LocalDateTime.now().plusDays(5),
                false);

        MvcResult result = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String id = objectMapper.readTree(responseContent).get("id").asText();

        mockMvc.perform(get("/coupon/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.code").value("GETTES"));
    }

    @Test
    void getCouponById_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        mockMvc.perform(get("/coupon/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCoupon_ShouldReturnNoContent_WhenExists() throws Exception {

        CreateCouponRequest request = new CreateCouponRequest(
                "DELTES",
                "Delete Test Coupon",
                new BigDecimal("15.0"),
                LocalDateTime.now().plusDays(15),
                true);

        MvcResult result = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String id = objectMapper.readTree(responseContent).get("id").asText();

        mockMvc.perform(delete("/coupon/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/coupon/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCoupon_ShouldReturnConflict_WhenCodeAlreadyExistsAndIsActive() throws Exception {

        CreateCouponRequest request1 = new CreateCouponRequest(
                "DUPTES",
                "Original Coupon",
                new BigDecimal("10.0"),
                LocalDateTime.now().plusDays(10),
                true);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        CreateCouponRequest request2 = new CreateCouponRequest(
                "DUPTES",
                "Duplicate Coupon",
                new BigDecimal("20.0"),
                LocalDateTime.now().plusDays(20),
                true);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isConflict());
    }

    @Test
    void createCoupon_ShouldReturnCreated_WhenCodeExistsButIsDeleted() throws Exception {

        CreateCouponRequest request1 = new CreateCouponRequest(
                "REUSED",
                "Original Coupon",
                new BigDecimal("10.0"),
                LocalDateTime.now().plusDays(10),
                true);

        MvcResult result = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String id = objectMapper.readTree(responseContent).get("id").asText();

        mockMvc.perform(delete("/coupon/" + id))
                .andExpect(status().isNoContent());

        CreateCouponRequest request2 = new CreateCouponRequest(
                "REUSED",
                "Reused Coupon",
                new BigDecimal("20.0"),
                LocalDateTime.now().plusDays(20),
                true);

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("REUSED"));
    }
}

package com.totvs.loancalculator.controllers;

import tools.jackson.databind.ObjectMapper;
import com.totvs.loancalculator.dtos.LoanRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCalculateLoan_Success() throws Exception
    {
        LoanRequest request = new LoanRequest(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 8, 1),
                BigDecimal.valueOf(10000.00),
                12
        );

        mockMvc.perform(post("/api/loan/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }

    @Test
    void testCalculateLoan_MissingInitialDate() throws Exception
    {
        LoanRequest request = new LoanRequest(
                null,
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 8, 1),
                BigDecimal.valueOf(10000.00),
                12
        );

        mockMvc.perform(post("/api/loan/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateLoan_NegativeLoanValue() throws Exception
    {
        LoanRequest request = new LoanRequest(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 8, 1),
                BigDecimal.valueOf(-100.00),
                12
        );

        mockMvc.perform(post("/api/loan/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateLoan_NegativeInterestRate() throws Exception
    {
        LoanRequest request = new LoanRequest(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 8, 1),
                BigDecimal.valueOf(10000.00),
                -5
        );

        mockMvc.perform(post("/api/loan/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
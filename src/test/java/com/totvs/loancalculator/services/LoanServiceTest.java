package com.totvs.loancalculator.services;

import com.totvs.loancalculator.dtos.LoanRequest;
import com.totvs.loancalculator.dtos.LoanResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceTest {

    private final LoanService loanService = new LoanService();

    @Test
    void testCalculateLoan_HappyPath()
    {
        LoanRequest request = new LoanRequest(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 8, 1),
                BigDecimal.valueOf(10000.00),
                12
        );

        List<LoanResponse> responses = loanService.calculateLoan(request);

        assertNotNull(responses);
        assertFalse(responses.isEmpty());

        boolean hasFirstInstallment = false;
        boolean hasLastInstallment = false;
        for (LoanResponse res : responses)
        {
            System.out.println("Consolidated Installment " + res.getConsolidatedInstallment());
            if ("1/3".equals(res.getConsolidatedInstallment()))
            {
                hasFirstInstallment = true;
                assertEquals("03/08/2026", res.getReferenceDate());
            }
            if ("3/3".equals(res.getConsolidatedInstallment()))
            {
                hasLastInstallment = true;
                assertEquals("01/10/2026", res.getReferenceDate()); // Final Date Oct 1, 2026 is Thursday
            }
        }

        assertTrue(hasFirstInstallment, "Should have first installment (adjusted for weekend)");
        assertTrue(hasLastInstallment, "Should have final installment");
    }

    @Test
    void testCalculateLoan_WeekendAdjustment()
    {
        LoanRequest request = new LoanRequest(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 11, 4),
                LocalDate.of(2026, 7, 4), // July 4 is Saturday
                BigDecimal.valueOf(5000.00),
                10
        );

        List<LoanResponse> responses = loanService.calculateLoan(request);

        assertNotNull(responses);

        LoanResponse firstInstallmentResponse = responses.stream()
                .filter(res -> res.getConsolidatedInstallment() != null && res.getConsolidatedInstallment().startsWith("1/"))
                .findFirst()
                .orElse(null);

        assertNotNull(firstInstallmentResponse, "First installment response should exist");
        assertEquals("06/07/2026", firstInstallmentResponse.getReferenceDate());
    }

    @Test
    void testCalculateLoan_HolidayAdjustment()
    {
        LoanRequest request = new LoanRequest(
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2027, 3, 25),
                LocalDate.of(2026, 12, 25),
                BigDecimal.valueOf(8000.00),
                8
        );

        List<LoanResponse> responses = loanService.calculateLoan(request);

        assertNotNull(responses);

        LoanResponse holidayResponse = responses.stream()
                .filter(res -> res.getConsolidatedInstallment() != null && res.getConsolidatedInstallment().startsWith("1/"))
                .findFirst()
                .orElse(null);

        assertNotNull(holidayResponse);
        assertEquals("28/12/2026", holidayResponse.getReferenceDate());
    }
}
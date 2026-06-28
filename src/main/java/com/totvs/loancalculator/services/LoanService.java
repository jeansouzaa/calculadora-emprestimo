package com.totvs.loancalculator.services;

import br.com.quantis.libraries.calendar.brazil.BrazilianCalendar;
import com.totvs.loancalculator.dtos.LoanRequest;
import com.totvs.loancalculator.dtos.LoanResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService
{
    private static final long ONE = 1L;
    private static final long TWO = 2L;
    private static final int DAY_BASIS = 360;
    private static final int CALC_SCALE = 10;
    private static final DateTimeFormatter BR_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<LoanResponse> calculateLoan(LoanRequest loanRequest)
    {
        LocalDate paymentDay = loanRequest.getFirstPaymentDate();
        BigDecimal interestProvision = BigDecimal.ZERO;
        BigDecimal interestAccrued = BigDecimal.ZERO;
        BigDecimal interestPaid = BigDecimal.ZERO;
        LocalDate lastReferenceDate = null;
        BigDecimal balance = loanRequest.getLoanValue();
        List<LoanResponse> loanResponses = new ArrayList<>();
        YearMonth yearMonthFirstPayment = YearMonth.from(loanRequest.getFirstPaymentDate());
        YearMonth yearMonthFinal = YearMonth.from(loanRequest.getFinalDate());
        long qtyInstallments = ChronoUnit.MONTHS.between(yearMonthFirstPayment, yearMonthFinal) + 1;
        int count = 1;
        BigDecimal loanValue = loanRequest.getLoanValue();
        for (LocalDate date = loanRequest.getInitialDate(); !date.isAfter(loanRequest.getFinalDate()); date = date.plusDays(ONE))
        {
            LocalDate lastDayMonth = date.with(TemporalAdjusters.lastDayOfMonth());
            if (date.equals(loanRequest.getInitialDate()) || date.equals(paymentDay) || date.equals(lastDayMonth) || date.equals(loanRequest.getFinalDate()))
            {
                String consolidateInstallment = "";
                if (date.equals(paymentDay) || date.equals(loanRequest.getFinalDate()))
                {
                    date = validateHolidaysAndSaturdayAndSunday(date);
                    consolidateInstallment = updateConsolidatedInstallment(count, qtyInstallments);
                    count++;
                    paymentDay = updatePaymentDay(paymentDay);
                }

                BigDecimal amortization = calculateAmortization(consolidateInstallment, loanRequest.getLoanValue(), qtyInstallments);
                BigDecimal loanBalance = calculateLoanBalance(balance, amortization);
                interestProvision = calculateInterestProvision(loanRequest.getInterestRate(), date, lastReferenceDate, balance, interestAccrued);
                interestPaid = calculateInterestPaid(consolidateInstallment, interestAccrued, interestProvision);
                interestAccrued = calculateInterestAccrued(interestAccrued, interestProvision, interestPaid);
                String referenceDate = date.format(BR_FORMAT);
                LoanResponse response = new LoanResponse(referenceDate, calculateOutstandingBalance(loanBalance, interestAccrued), consolidateInstallment, loanValue, calculateTotalInstallmentAmount(amortization, interestPaid), amortization, loanBalance, interestProvision, interestAccrued, interestPaid);
                loanResponses.add(response);
                balance = loanBalance;
                lastReferenceDate = date;
            }
            loanValue = BigDecimal.ZERO;
        }
        return loanResponses;
    }

    private String updateConsolidatedInstallment(int count, long qtyInstallments)
    {
        return count + "/" + qtyInstallments;
    }

    private LocalDate updatePaymentDay(LocalDate paymentDay)
    {
        return paymentDay.plusMonths(ONE);
    }

    private LocalDate validateHolidaysAndSaturdayAndSunday(LocalDate date)
    {
        while(true)
        {
            if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY))
            {
                date = date.plusDays(TWO);
            }
            else if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY) || BrazilianCalendar.isBankingHoliday(date))
            {
                date = date.plusDays(ONE);
            }
            else
            {
                break;
            }
        }
        return date;
    }

    private BigDecimal calculateTotalInstallmentAmount(BigDecimal amortization, BigDecimal interestPaid)
    {
        return sumTwoDoubles(amortization, interestPaid);
    }

    private BigDecimal sumTwoDoubles(BigDecimal firstDoubleValue, BigDecimal secondDoubleValue)
    {
        return firstDoubleValue.add(secondDoubleValue);
    }

    private BigDecimal calculateOutstandingBalance(BigDecimal loanBalance, BigDecimal interestAccrued)
    {
        return sumTwoDoubles(loanBalance, interestAccrued);
    }

    private BigDecimal calculateLoanBalance(BigDecimal balance, BigDecimal amortization)
    {
        return balance.subtract(amortization);
    }

    private BigDecimal calculateAmortization(String consolidatedInstallment, BigDecimal loanValue, long qtyInstallments)
    {
        return consolidatedInstallment != null && !consolidatedInstallment.trim().isEmpty() ? loanValue.divide(BigDecimal.valueOf(qtyInstallments), CALC_SCALE, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    private BigDecimal calculateInterestAccrued(BigDecimal interestAccrued, BigDecimal interestProvision, BigDecimal interestPaid)
    {
        return sumTwoDoubles(interestAccrued, interestProvision).subtract(interestPaid);
    }

    private BigDecimal calculateInterestPaid(String consolidatedInstallment, BigDecimal interestAccrued, BigDecimal interestProvision)
    {
        return consolidatedInstallment != null && !consolidatedInstallment.trim().isEmpty() ? sumTwoDoubles(interestAccrued, interestProvision) : BigDecimal.ZERO;
    }

    private BigDecimal calculateInterestProvision(int interestRate, LocalDate referenceDateActual, LocalDate lastReferenceDate, BigDecimal balance, BigDecimal interestAccrued)
    {
        if (referenceDateActual == null || lastReferenceDate == null) return BigDecimal.ZERO;
        int qtyDays = getQtyDaysBetweenDates(lastReferenceDate, referenceDateActual);
        double interestRateDecimal = interestRate;
        interestRateDecimal = interestRateDecimal / 100 + 1;
        return sumTwoDoubles(balance, interestAccrued).multiply(BigDecimal.valueOf((Math.pow(interestRateDecimal, (double) qtyDays / DAY_BASIS) - 1)));
    }

    private int getQtyDaysBetweenDates(LocalDate lastReferenceDate, LocalDate referenceDateActual)
    {
        return (int) ChronoUnit.DAYS.between(lastReferenceDate, referenceDateActual);
    }
}
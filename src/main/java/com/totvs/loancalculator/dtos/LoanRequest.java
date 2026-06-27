package com.totvs.loancalculator.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanRequest
{
    @NotNull(message = "A data inicial é obrigatória!")
    private LocalDate initialDate;
    @NotNull(message = "A data final é obrigatória!")
    private LocalDate finalDate;
    @NotNull(message = "A data de primeiro pagamento é obrigatória!")
    private LocalDate firstPaymentDate;
    @NotNull(message = "O valor do empréstimo é obrigatório!")
    @Positive(message = "O valor do empréstimo deve ser maior que zero!")
    private BigDecimal loanValue;
    @NotNull(message = "A taxa de juros é obrigatória!")
    @Positive(message = "A taxa de juros deve ser maior que zero!")
    private int interestRate;

    public LoanRequest(LocalDate initialDate, LocalDate finalDate, LocalDate firstPaymentDate, BigDecimal loanValue, int interestRate)
    {
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.firstPaymentDate = firstPaymentDate;
        this.loanValue = loanValue;
        this.interestRate = interestRate;
    }

    public LocalDate getInitialDate()
    {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate)
    {
        this.initialDate = initialDate;
    }

    public LocalDate getFinalDate()
    {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate)
    {
        this.finalDate = finalDate;
    }

    public LocalDate getFirstPaymentDate()
    {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(LocalDate firstPaymentDate)
    {
        this.firstPaymentDate = firstPaymentDate;
    }

    public BigDecimal getLoanValue()
    {
        return loanValue;
    }

    public void setLoanValue(BigDecimal loanValue)
    {
        this.loanValue = loanValue;
    }

    public int getInterestRate()
    {
        return interestRate;
    }

    public void setInterestRate(int interestRate)
    {
        this.interestRate = interestRate;
    }
}
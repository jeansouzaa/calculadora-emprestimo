package com.totvs.loancalculator.dtos;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoanResponse
{
    private String referenceDate;

    private BigDecimal outstandingBalance;

    private String consolidatedInstallment;

    private BigDecimal loanValue;

    private BigDecimal totalInstallmentAmount;

    private BigDecimal amortization;

    private BigDecimal loanBalance;

    private BigDecimal interestProvision;

    private BigDecimal interestAccrued;

    private BigDecimal interestPaid;

    public LoanResponse(String referenceDate, BigDecimal outstandingBalance, String consolidatedInstallment, BigDecimal loanValue, BigDecimal totalInstallmentAmount, BigDecimal amortization, BigDecimal loanBalance, BigDecimal interestProvision, BigDecimal interestAccrued, BigDecimal interestPaid)
    {
        this.referenceDate = referenceDate;
        this.outstandingBalance = outstandingBalance.setScale(2, RoundingMode.HALF_UP);
        this.consolidatedInstallment = consolidatedInstallment;
        this.loanValue = loanValue.setScale(2, RoundingMode.HALF_UP);
        this.totalInstallmentAmount = totalInstallmentAmount.setScale(2, RoundingMode.HALF_UP);
        this.amortization = amortization.setScale(2, RoundingMode.HALF_UP);
        this.loanBalance = loanBalance.setScale(2, RoundingMode.HALF_UP);
        this.interestProvision = interestProvision.setScale(2, RoundingMode.HALF_UP);
        this.interestAccrued = interestAccrued.setScale(2, RoundingMode.HALF_UP);
        this.interestPaid = interestPaid.setScale(2, RoundingMode.HALF_UP);
    }

    public LoanResponse(){}

    public String getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getConsolidatedInstallment() {
        return consolidatedInstallment;
    }

    public void setConsolidatedInstallment(String consolidatedInstallment) {
        this.consolidatedInstallment = consolidatedInstallment;
    }

    public BigDecimal getLoanValue() {
        return loanValue;
    }

    public void setLoanValue(BigDecimal loanValue) {
        this.loanValue = loanValue;
    }

    public BigDecimal getTotalInstallmentAmount() {
        return totalInstallmentAmount;
    }

    public void setTotalInstallmentAmount(BigDecimal totalInstallmentAmount) {
        this.totalInstallmentAmount = totalInstallmentAmount;
    }

    public BigDecimal getAmortization() {
        return amortization;
    }

    public void setAmortization(BigDecimal amortization) {
        this.amortization = amortization;
    }

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    public BigDecimal getInterestProvision() {
        return interestProvision;
    }

    public void setInterestProvision(BigDecimal interestProvision) {
        this.interestProvision = interestProvision;
    }

    public BigDecimal getInterestAccrued() {
        return interestAccrued;
    }

    public void setInterestAccrued(BigDecimal interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
    }
}

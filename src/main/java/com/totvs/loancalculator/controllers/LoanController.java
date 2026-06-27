package com.totvs.loancalculator.controllers;

import com.totvs.loancalculator.dtos.LoanRequest;
import com.totvs.loancalculator.dtos.LoanResponse;
import com.totvs.loancalculator.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/loan/")
public class LoanController
{
    @Autowired
    LoanService loanService;

    @PostMapping("calculate")
    public List<LoanResponse> calculateLoan(@RequestBody LoanRequest loanRequest)
    {
        return loanService.calculateLoan(loanRequest);
    }
}

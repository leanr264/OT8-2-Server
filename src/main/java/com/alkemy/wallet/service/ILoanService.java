package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.request.LoanRequestDto;
import com.alkemy.wallet.dto.response.LoanResponseDto;

public interface ILoanService {
    LoanResponseDto simulateLoan(LoanRequestDto loanRequest);
    LoanResponseDto applyLoan(LoanRequestDto loanRequest, String token);
}

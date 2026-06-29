package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.request.LoanRequestDto;
import com.alkemy.wallet.dto.LoanDto;
import com.alkemy.wallet.dto.response.LoanResponseDto;

import java.util.List;

public interface ILoanService {
    List<LoanDto> getLoansByUserId(Long id, String token);
    LoanResponseDto simulateLoan(LoanRequestDto loanRequest);
    LoanResponseDto applyLoan(LoanRequestDto loanRequest, String token);
}

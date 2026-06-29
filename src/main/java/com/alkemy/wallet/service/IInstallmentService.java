package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.InstallmentDto;
import com.alkemy.wallet.dto.request.InstallmentRequestDto;
import com.alkemy.wallet.dto.response.InstallmentResponseDto;
import com.alkemy.wallet.dto.response.LoanResponseDto;
import com.alkemy.wallet.entity.Installment;
import com.alkemy.wallet.entity.Loan;

import java.util.List;

public interface IInstallmentService {
    InstallmentResponseDto payInstallment(InstallmentRequestDto installmentRequest, String token);
    public List<Installment> generateInstallments(Loan loan, LoanResponseDto loanResponse);
    List<InstallmentDto> getInstallmentsByLoanId(Long loanId, String token);
}

package com.alkemy.wallet.dto;

import com.alkemy.wallet.dto.response.InstallmentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class LoanDto {
    private Long loanId;
    private Double amount;
    private int months;
    private String status;
}

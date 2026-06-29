package com.alkemy.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoanResponseDto {
    double amount;
    int months;
    String interestRate;
    double paymentPerMonth;
    double totalInterest;
    double totalPayment;
}

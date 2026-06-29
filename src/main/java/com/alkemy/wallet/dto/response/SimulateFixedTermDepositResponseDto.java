package com.alkemy.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Setter
@Getter
public class SimulateFixedTermDepositResponseDto {
    double investedAmount;
    Timestamp creationDate;
    Timestamp closingDate;
    double  totalInterest;
    double totalValue;
}

package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class FixedTermDepositDto {
    private Long accountId;
    private Long fixedTermDepositId;
    private double amount;
    private double interest;
    private Timestamp creationDate;
    private Timestamp closingDate;
}

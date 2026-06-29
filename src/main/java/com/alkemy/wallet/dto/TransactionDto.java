package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class TransactionDto {
    private Long accountId;
    private String currency;
    private Long transactionId;
    private double amount;
    private String type;
    private String description;
    private Timestamp transactionDate;
}

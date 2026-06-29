package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter

public class AccountDto {
    private String userEmail;
    private Long accountId;
    private String currency;
    private double transactionLimit;
    private double balance;
    private Timestamp creationDate;
}

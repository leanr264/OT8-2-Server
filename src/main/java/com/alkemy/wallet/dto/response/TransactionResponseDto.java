package com.alkemy.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class TransactionResponseDto {
    private String userEmail;
    private Long accountId;
    private Long transactionId;
    private String currency;
    private String transactionType;
    private Double amount;
    private String description;
    private Timestamp creationDate;
}

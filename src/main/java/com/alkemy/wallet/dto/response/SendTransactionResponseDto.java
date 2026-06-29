package com.alkemy.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendTransactionResponseDto {
    private String originUserEmail;
    private String destinyUserEmail;
    private Long originAccountId;
    private Long destinyAccountId;
    private Long transactionId;
    private Double amount;
    private String TransactionType;
    private String description;
    private Timestamp transactionDate;
}

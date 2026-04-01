package com.alkemy.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class CurrencyExchangeResponseDTO {
    private String userEmail;
    private Long sourceAccountId;
    private Long targetAccountId;
    private Long transactionId;
    private String transactionType;
    private Double amountArs;
    private Double amountUsd;
    private String description;
    private Timestamp transactionDate;
}

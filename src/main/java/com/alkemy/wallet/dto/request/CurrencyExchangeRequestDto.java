package com.alkemy.wallet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CurrencyExchangeRequestDto {
    @Positive
    @NotNull
    private Double amountUsd;
    private String description;
}

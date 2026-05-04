package com.alkemy.wallet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoanRequestDto {
    @NotNull
    @Positive
    double amount;
    @NotNull
    @NotBlank
    int months;
}

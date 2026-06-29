package com.alkemy.wallet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendTransactionRequestDto {
    @NotNull
    private Long destinyAccountId;
    @NotNull
    @Positive
    private Double amount;
    private String description;
}

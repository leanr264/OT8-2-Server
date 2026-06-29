package com.alkemy.wallet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SimulateFixedTermDepositRequestDto {
    double amount;
    int days;
}

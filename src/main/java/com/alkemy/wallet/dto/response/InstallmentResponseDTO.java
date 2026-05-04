package com.alkemy.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InstallmentResponseDTO {
    int installmentNumber;
    double amountPay;
    String status;
}

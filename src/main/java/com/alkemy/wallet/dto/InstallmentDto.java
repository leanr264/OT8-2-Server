package com.alkemy.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class InstallmentDto {
    private Long id;
    private int installmentNumber;
    private double amountPay;
    private String status;
    private LocalDate expirationDate;
}

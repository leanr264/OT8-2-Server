package com.alkemy.wallet.dto.response;

import com.alkemy.wallet.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PageableTransactionResponseDto {
    private long count;
    private int pages;
    private String prev;
    private String next;
    private List<TransactionDto> results;
}

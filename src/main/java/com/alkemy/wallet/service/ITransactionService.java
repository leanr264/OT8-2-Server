package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.dto.request.CurrencyExchangeRequestDto;
import com.alkemy.wallet.dto.request.SendTransactionRequestDto;
import com.alkemy.wallet.dto.request.UpdateTransactionRequestDto;
import com.alkemy.wallet.dto.request.TransactionRequestDto;
import com.alkemy.wallet.dto.response.CurrencyExchangeResponseDTO;
import com.alkemy.wallet.dto.response.PageableTransactionResponseDto;
import com.alkemy.wallet.dto.response.SendTransactionResponseDto;
import com.alkemy.wallet.dto.response.TransactionResponseDto;

public interface ITransactionService {
    TransactionDto updateTransactionDescription(Long id, UpdateTransactionRequestDto updateRequest, String token);
    TransactionResponseDto getTransaction(Long id, String token);
    PageableTransactionResponseDto getTransactionsByUserId(Long userId, int page,String token);
    TransactionResponseDto createDeposit(TransactionRequestDto depositRequest, String token);
    TransactionResponseDto createPayment(TransactionRequestDto paymentRequest, String token);
    SendTransactionResponseDto sendArs(SendTransactionRequestDto transactionRequest, String token);
    SendTransactionResponseDto sendUsd(SendTransactionRequestDto transactionRequest, String token);
    CurrencyExchangeResponseDTO buyUsd(SendTransactionRequestDto transactionRequest, String token);
    CurrencyExchangeResponseDTO sellUsd(CurrencyExchangeRequestDto transactionRequest, String token);
}

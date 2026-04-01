package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.dto.request.SendTransactionRequestDto;
import com.alkemy.wallet.dto.request.UpdateTransactionRequestDto;
import com.alkemy.wallet.dto.request.TransactionRequestDto;
import com.alkemy.wallet.dto.response.CurrencyExchangeResponseDTO;
import com.alkemy.wallet.dto.response.PageableTransactionResponseDto;
import com.alkemy.wallet.dto.response.SendTransactionResponseDto;
import com.alkemy.wallet.dto.response.TransactionResponseDto;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.TransactionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final ITransactionService transactionService;

    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransaction(@PathVariable Long id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        TransactionResponseDto transactionResponse = transactionService.getTransaction(id,token);
        return new ResponseEntity<>(transactionResponse,HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<PageableTransactionResponseDto>getTransactionsByUserId(@RequestParam(name="user") Long userId, @RequestParam(defaultValue = "0") int page,@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        PageableTransactionResponseDto transactionsResponse = transactionService.getTransactionsByUserId(userId,page,token);
        return new ResponseEntity<>(transactionsResponse, HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<TransactionDto>updateTransactionDescription(@PathVariable Long id, @Valid @RequestBody UpdateTransactionRequestDto updateRequest,
                                                         @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        TransactionDto transactionDto = transactionService.updateTransactionDescription(id,updateRequest,token);
        return new ResponseEntity<>(transactionDto, HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> createDeposit(@Valid @RequestBody TransactionRequestDto depositRequest, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        TransactionResponseDto transactionResponse = transactionService.createDeposit(depositRequest,token);
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }

    @PostMapping("/payment")
    public ResponseEntity<TransactionResponseDto> createPayment(@Valid @RequestBody TransactionRequestDto paymentRequest, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        TransactionResponseDto paymentResponse = transactionService.createPayment(paymentRequest,token);
        return new ResponseEntity<>(paymentResponse,HttpStatus.CREATED);
    }

    @PostMapping("/sendArs")
    public ResponseEntity<SendTransactionResponseDto> sendArs(@Valid @RequestBody SendTransactionRequestDto transactionRequest, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        SendTransactionResponseDto transactionResponse = transactionService.sendArs(transactionRequest,token);
        return new ResponseEntity<>(transactionResponse,HttpStatus.CREATED);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<SendTransactionResponseDto> sendUsd(@Valid @RequestBody SendTransactionRequestDto transactionRequest, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        SendTransactionResponseDto transactionResponse = transactionService.sendUsd(transactionRequest,token);
        return new ResponseEntity<>(transactionResponse,HttpStatus.CREATED);
    }

    @PostMapping("/buyUsd")
    public ResponseEntity<CurrencyExchangeResponseDTO> buyUsd(@Valid @RequestBody SendTransactionRequestDto transactionRequest, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        CurrencyExchangeResponseDTO transactionResponse = transactionService.buyUsd(transactionRequest, token);
        return new ResponseEntity<>(transactionResponse, HttpStatus.CREATED);
    }
}

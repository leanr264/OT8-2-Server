package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.request.LoanRequestDto;
import com.alkemy.wallet.dto.response.LoanResponseDto;
import com.alkemy.wallet.dto.LoanDto;
import com.alkemy.wallet.service.ILoanService;
import com.alkemy.wallet.service.LoanServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    private final ILoanService loanService;

    public LoanController(LoanServiceImpl loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<LoanDto>> getMyLoans(@PathVariable Long id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        List<LoanDto> loansDto = loanService.getLoansByUserId(id, token);
        return new ResponseEntity<>(loansDto, HttpStatus.OK);
    }

    @PostMapping("/simulate")
    public ResponseEntity<LoanResponseDto> simulateLoan(@RequestBody LoanRequestDto loanRequest){
        LoanResponseDto loanResponse = loanService.simulateLoan(loanRequest);
        return new ResponseEntity<>(loanResponse, HttpStatus.OK);
    }

    @PostMapping("/apply")
    public ResponseEntity<LoanResponseDto> applyLoan(@RequestBody LoanRequestDto loanRequest, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        LoanResponseDto loanResponse = loanService.applyLoan(loanRequest, token);
        return new ResponseEntity<>(loanResponse, HttpStatus.OK);
    }
}

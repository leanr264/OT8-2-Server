package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.request.SimulateFixedTermDepositRequestDto;
import com.alkemy.wallet.dto.response.SimulateFixedTermDepositResponseDto;
import com.alkemy.wallet.service.FixedTermDepositServiceImpl;
import com.alkemy.wallet.service.IFixedTermDepositService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fixedTerm")
public class FixedTermDepositController {
    private final IFixedTermDepositService fixedTermService;

    public FixedTermDepositController(FixedTermDepositServiceImpl fixedTermService) {
        this.fixedTermService = fixedTermService;
    }

    @PostMapping("/simulate")
    public ResponseEntity<SimulateFixedTermDepositResponseDto> simulateFixedTermDeposit(@RequestBody SimulateFixedTermDepositRequestDto fixedTermRequest){
        SimulateFixedTermDepositResponseDto fixedTermResponse = fixedTermService.simulateFixedTermDeposit(fixedTermRequest);
        return new ResponseEntity<>(fixedTermResponse, HttpStatus.OK);
    }
}

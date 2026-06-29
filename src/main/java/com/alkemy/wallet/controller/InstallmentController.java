package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.InstallmentDto;
import com.alkemy.wallet.dto.request.InstallmentRequestDto;
import com.alkemy.wallet.dto.response.InstallmentResponseDto;
import com.alkemy.wallet.service.IInstallmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans/installments")
public class InstallmentController {
    private final IInstallmentService installmentService;

    public InstallmentController(IInstallmentService installmentService){
        this.installmentService = installmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<InstallmentDto>> getMyInstallments(@PathVariable Long id, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        List<InstallmentDto> installmentsDto = installmentService.getInstallmentsByLoanId(id, token);
        return new ResponseEntity<>(installmentsDto, HttpStatus.OK);
    }

    @PostMapping("/pay")
    public ResponseEntity<InstallmentResponseDto> payInstallment(@RequestBody InstallmentRequestDto installmentRequest, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        InstallmentResponseDto installmentResponse = installmentService.payInstallment(installmentRequest, token);
        return new ResponseEntity<>(installmentResponse, HttpStatus.OK);
    }
}

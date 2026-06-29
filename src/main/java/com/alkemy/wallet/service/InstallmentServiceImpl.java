package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.InstallmentDto;
import com.alkemy.wallet.dto.request.InstallmentRequestDto;
import com.alkemy.wallet.dto.request.TransactionRequestDto;
import com.alkemy.wallet.dto.response.InstallmentResponseDto;
import com.alkemy.wallet.dto.response.LoanResponseDto;
import com.alkemy.wallet.entity.Account;
import com.alkemy.wallet.entity.Installment;
import com.alkemy.wallet.entity.Loan;
import com.alkemy.wallet.enums.ECurrency;
import com.alkemy.wallet.enums.EInstallmentStatus;
import com.alkemy.wallet.enums.ELoanStatus;
import com.alkemy.wallet.repository.IInstallmentRepository;
import com.alkemy.wallet.repository.ILoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstallmentServiceImpl implements IInstallmentService{
    private final IInstallmentRepository installmentRepository;
    private final ILoanRepository loanRepository;
    private final ITransactionService transactionService;

    public InstallmentServiceImpl(IInstallmentRepository installmentRepository, ILoanRepository loanRepository, ITransactionService transactionService) {
        this.installmentRepository = installmentRepository;
        this.loanRepository = loanRepository;
        this.transactionService = transactionService;
    }

    @Override
    public List<Installment> generateInstallments(Loan loan, LoanResponseDto loanResponse) {
        List<Installment> installments = new ArrayList<>();

        int months = loanResponse.getMonths();
        double amount = loanResponse.getPaymentPerMonth();
        double accumulated = 0;
        LocalDateTime now = LocalDateTime.now();

        for(int m = 0; m < months; m++) {
            Installment installment = new Installment();

            installment.setInstallmentNumber(m+1);

            if(m == months - 1) {
                amount = loanResponse.getTotalPayment() - accumulated;
            } else {
                accumulated += amount;
            }

            installment.setAmount(amount);
            installment.setStatus(EInstallmentStatus.PENDING);
            installment.setLoan(loan);
            installment.setExpirationDate(LocalDate.from(now.plusMonths(m+1)));
            installments.add(installment);
        }
        return installments;
    }

    @Transactional
    @Override
    public InstallmentResponseDto payInstallment(InstallmentRequestDto installmentRequest, String token){
        Installment installment = installmentRepository.findById(installmentRequest.getInstallmentId())
                        .orElseThrow(() -> new RuntimeException("Installment not found"));

        if (installment.getStatus() == EInstallmentStatus.PAID) {
            throw new RuntimeException("Installment already paid");
        }

        Loan loan = installment.getLoan();
        Account account = loan.getAccount();
        double amount = installment.getAmount();

        if (account.getBalance() >= amount && installment.getStatus() == EInstallmentStatus.PENDING) {
            TransactionRequestDto paymentRequest = new TransactionRequestDto();
            paymentRequest.setAmount(amount);
            paymentRequest.setCurrency(String.valueOf(ECurrency.ARS));
            paymentRequest.setDescription("Pago de cuota de préstamo - cuota N°: " + installment.getInstallmentNumber());
            transactionService.createPayment(paymentRequest, token);

            installment.setStatus(EInstallmentStatus.PAID);
            installment.setPaymentDate(LocalDate.now());
            installmentRepository.save(installment);

            List<Installment> installments =
                    installmentRepository.findByLoanId(loan.getId());

            boolean allPaid = installments.stream()
                    .allMatch(i -> i.getStatus() == EInstallmentStatus.PAID);

            if (allPaid) {
                loan.setStatus(ELoanStatus.PAID);
                loanRepository.save(loan);
            }

            return new InstallmentResponseDto(
              installment.getInstallmentNumber(),
              installment.getAmount(),
              installment.getStatus().toString()
            );
        }
        return null;
    }

    @Override
    public List<InstallmentDto> getInstallmentsByLoanId(Long loanId, String token) {
        List<Installment> installments = installmentRepository.findByLoanId(loanId);

        return installments.stream()
                .map(this::mapToInstallmentDto)
                .toList();
    }

    private InstallmentDto mapToInstallmentDto(Installment installment) {
        return new InstallmentDto(
                installment.getId(),
                installment.getInstallmentNumber(),
                installment.getAmount(),
                installment.getStatus().toString(),
                installment.getExpirationDate()
        );
    }
}



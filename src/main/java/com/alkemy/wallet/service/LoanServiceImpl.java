package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.request.LoanRequestDto;
import com.alkemy.wallet.dto.request.SendTransactionRequestDto;
import com.alkemy.wallet.dto.request.TransactionRequestDto;
import com.alkemy.wallet.dto.response.InstallmentResponseDTO;
import com.alkemy.wallet.dto.response.LoanResponseDto;
import com.alkemy.wallet.entity.Account;
import com.alkemy.wallet.entity.Installment;
import com.alkemy.wallet.entity.Loan;
import com.alkemy.wallet.entity.User;
import com.alkemy.wallet.enums.ECurrency;
import com.alkemy.wallet.enums.EInstallmentStatus;
import com.alkemy.wallet.enums.ELoanStatus;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IInstallmentRepository;
import com.alkemy.wallet.repository.ILoanRepository;
import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements ILoanService{
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;
    private final ILoanRepository loanRepository;
    private final IInstallmentRepository installmentRepository;
    private final IJwtService jwtService;
    private final ITransactionService transactionService;

    public LoanServiceImpl(IUserRepository userRepository, IAccountRepository accountRepository, ILoanRepository loanRepository, IInstallmentRepository installmentRepository, IJwtService jwtService, ITransactionService transactionService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.loanRepository = loanRepository;
        this.installmentRepository = installmentRepository;
        this.jwtService = jwtService;
        this.transactionService = transactionService;
    }

    @Override
    public LoanResponseDto simulateLoan(LoanRequestDto loanRequest) {
        double amount = loanRequest.getAmount();
        int months = loanRequest.getMonths();
        if(amount > 0.0 && months > 0){
            return calculateLoan(amount,months);
        }
        return null;
    }

    @Transactional
    @Override
    public LoanResponseDto applyLoan(LoanRequestDto loanRequest, String token) {
        String userMail = jwtService.extractUsername(token.substring(7));

        Optional<User> userOptional = userRepository.findByEmail(userMail);

        if(userOptional.isPresent()){
            User user = userOptional.get();

            Optional<Account> accountOptional = user.getAccounts().stream()
                    .filter(account -> account.getCurrency() == ECurrency.ARS)
                    .findFirst();
            if(accountOptional.isPresent()){
                Account account = accountOptional.get();
                if(account.getBalance() >= loanRequest.getAmount() && account.getTransactionLimit() >= loanRequest.getAmount() && loanRequest.getAmount() >= 0.0){
                    Loan newLoan = new Loan();
                    newLoan.setAmount(loanRequest.getAmount());
                    newLoan.setMonths(loanRequest.getMonths());
                    newLoan.setStatus(ELoanStatus.APPROVED);
                    newLoan.setAccount(account);
                    Loan loanApplicated = loanRepository.save(newLoan);

                    TransactionRequestDto incomeRequest = new TransactionRequestDto();
                    incomeRequest.setAmount(loanRequest.getAmount());
                    incomeRequest.setCurrency("ARS");
                    incomeRequest.setDescription("Préstamo aprobado");
                    transactionService.createIncome(incomeRequest, token);

                    return calculateLoan(loanApplicated.getAmount(), loanApplicated.getMonths());
                }
            }
        }
        return null;
    }

    public InstallmentResponseDTO payInstallment() {
        return null;
    }

    private LoanResponseDto calculateLoan(double amount, int months) {
        double interest = switch (months) {
            case 3 -> 0.05;
            case 6 -> 0.10;
            case 9 -> 0.15;
            case 12 -> 0.20;
            default -> 0.00;
        };

        double paymentPerMonth = amount/months + amount * interest;
        double totalInterest = (amount*interest)*months;
        double totalPayment = amount + totalInterest;
        return new LoanResponseDto(
                amount,
                months,
                interest*100 + "% monthly",
                paymentPerMonth,
                totalInterest,
                totalPayment
        );
    }

    private List<Installment> generateInstallments(Loan loan, LoanResponseDto loanResponse) {
        List<Installment> installments = new ArrayList<>();

        int months = loanResponse.getMonths();
        double amount = loanResponse.getPaymentPerMonth();
        LocalDateTime now = LocalDateTime.now();

        for(int m = 0; m < months; m++) {
            Installment installment = new Installment();

            installment.setInstallmentNumber(m+1);
            installment.setAmount(amount);
            installment.setStatus(EInstallmentStatus.PENDING);
            installment.setLoan(loan);
            installment.setExpirationDate(LocalDate.from(now.plusMonths(m+1)));
            installments.add(installment);
            //installmentRepository.save(installment);
        }
        return installments;
    }
}

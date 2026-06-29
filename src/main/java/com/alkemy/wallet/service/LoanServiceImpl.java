package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.request.LoanRequestDto;
import com.alkemy.wallet.dto.request.TransactionRequestDto;
import com.alkemy.wallet.dto.response.InstallmentResponseDto;
import com.alkemy.wallet.dto.LoanDto;
import com.alkemy.wallet.dto.response.LoanResponseDto;
import com.alkemy.wallet.entity.Account;
import com.alkemy.wallet.entity.Installment;
import com.alkemy.wallet.entity.Loan;
import com.alkemy.wallet.entity.User;
import com.alkemy.wallet.enums.ECurrency;
import com.alkemy.wallet.enums.ELoanStatus;
import com.alkemy.wallet.repository.IInstallmentRepository;
import com.alkemy.wallet.repository.ILoanRepository;
import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LoanServiceImpl implements ILoanService{
    private final IUserRepository userRepository;
    private final ILoanRepository loanRepository;
    private final IInstallmentRepository installmentRepository;
    private final IJwtService jwtService;
    private final ITransactionService transactionService;
    private final IInstallmentService installmentService;

    public LoanServiceImpl(IUserRepository userRepository, ILoanRepository loanRepository, IInstallmentRepository installmentRepository, IJwtService jwtService, ITransactionService transactionService, IInstallmentService installmentService) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.installmentRepository = installmentRepository;
        this.jwtService = jwtService;
        this.transactionService = transactionService;
        this.installmentService = installmentService;
    }

    @Override
    public List<LoanDto> getLoansByUserId(Long id, String token) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String userEmail = jwtService.extractUsername(token.substring(7));

            if(Objects.equals(user.getEmail(), userEmail)){
                List<Loan> loans = loanRepository.findAllByAccountUser(user);

                return loans.stream()
                        .map(this::mapToLoanDto)
                        .toList();
            }
        }
        return null;
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
                    Loan savedLoan = loanRepository.save(newLoan);

                    LoanResponseDto loanResponse = calculateLoan(savedLoan.getAmount(), savedLoan.getMonths());

                    List<Installment> installments = installmentService.generateInstallments(savedLoan, loanResponse);
                    installmentRepository.saveAll(installments);
                    savedLoan.setInstallments(installments);

                    TransactionRequestDto incomeRequest = new TransactionRequestDto();
                    incomeRequest.setAmount(loanRequest.getAmount());
                    incomeRequest.setCurrency("ARS");
                    incomeRequest.setDescription("Préstamo aprobado");
                    transactionService.createIncome(incomeRequest, token);

                    return loanResponse;
                }
            }
        }
        return null;
    }

    private LoanDto mapToLoanDto(Loan loan) {
        return new LoanDto(
                loan.getId(),
                loan.getAmount(),
                loan.getMonths(),
                loan.getStatus().name()
        );
    }

    private LoanResponseDto calculateLoan(double amount, int months) {
        double interest = switch (months) {
            case 3 -> 0.05;
            case 6 -> 0.10;
            case 9 -> 0.15;
            case 12 -> 0.20;
            default -> 0.00;
        };

        double totalInterest = amount * interest;
        double totalPayment = amount + totalInterest;
        double paymentPerMonth = totalPayment / months;
        return new LoanResponseDto(
                amount,
                months,
                interest*100 + "% mensual",
                paymentPerMonth,
                totalInterest,
                totalPayment
        );
    }
}

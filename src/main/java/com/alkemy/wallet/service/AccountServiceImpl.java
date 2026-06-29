package com.alkemy.wallet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.alkemy.wallet.dto.BalanceDto;
import com.alkemy.wallet.dto.request.UpdateAccountRequestDto;
import com.alkemy.wallet.dto.response.PageableAccountResponseDto;
import com.alkemy.wallet.enums.ERole;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.entity.FixedTermDeposit;
import com.alkemy.wallet.entity.Transaction;
import com.alkemy.wallet.enums.ECurrency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.entity.Account;
import com.alkemy.wallet.entity.User;
import com.alkemy.wallet.repository.IUserRepository;

@Service
public class AccountServiceImpl implements IAccountService {
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;
    private final IJwtService jwtService;

    public AccountServiceImpl(IUserRepository userRepository, IAccountRepository accountRepository, JwtServiceImpl jwtService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
    }

    @Override
    public PageableAccountResponseDto getAllAccounts(int page) {
        int pageToFind = page > 0 ? page-1 : 0;
        PageRequest pr = PageRequest.of(pageToFind,10);
        Page<Account> accountPage = accountRepository.findAll(pr);
        long count = accountPage.getTotalElements();
        int pages = accountPage.getTotalPages();
        String prevPage = accountPage.hasPrevious() ? "/api/v1/users?page="+(page-1) : null;
        String nextPage = accountPage.hasNext() ? "/api/v1/users?page="+(page+1) : null;
        if(pages < page){
            return null;
        }
        List<Account> accounts = accountPage.getContent();
        List<AccountDto> accountsDto = accounts.stream().map(account -> {
            return new AccountDto(
                    account.getUser().getEmail(),
                    account.getId(),
                    account.getCurrency().name(),
                    account.getBalance(),
                    account.getTransactionLimit(),
                    account.getCreationDate()
            );
        }).toList();
        return new PageableAccountResponseDto(
                count,
                pages,
                prevPage,
                nextPage,
                accountsDto
        );
    }

    @Override
    public List<AccountDto> getAccountsByUserId(Long id,String token) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String userEmail = jwtService.extractUsername(token.substring(7));
            if(Objects.equals(user.getEmail(), userEmail) || user.getRole().getName() == ERole.ADMIN){
                List<Account> accounts = user.getAccounts();
                List<AccountDto> accountsDto = new ArrayList<>();
                for (Account account : accounts) {
                    AccountDto accountDto = new AccountDto(
                            user.getEmail(),
                            account.getId(),
                            account.getCurrency().name(),
                            account.getTransactionLimit(),
                            account.getBalance(),
                            account.getCreationDate()
                    );
                    accountsDto.add(accountDto);
                }
                return accountsDto;
            }
        }
        return null;
    }
    @Override
    public List<BalanceDto> getBalance(String token){
        String userEmail = jwtService.extractUsername(token.substring(7));
        Optional<User> optionalUser=userRepository.findByEmail(userEmail);
        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            List<Account> accounts=user.getAccounts();
            List<BalanceDto> balancesDto= new ArrayList<>();
            for (Account account : accounts){
                BalanceDto balanceDto= new BalanceDto(
                        userEmail,
                        account.getId(),
                        account.getCurrency().name(),
                        account.getBalance(),
                        getHistory(account.getTransactions(),account.getId()),
                        getFixedTerms(account.getFixedTermDeposits(),account.getId())
                );
                balancesDto.add(balanceDto);
            }
            return balancesDto;
        }
        return null;
    }


    @Override
    public AccountDto updateTransactionLimit(Long id, UpdateAccountRequestDto updateRequest, String token) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            String userEmail = jwtService.extractUsername(token.substring(7));
            Optional<User> userOptional = userRepository.findByEmail(userEmail);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                if(Objects.equals(account.getUser().getId(), user.getId())){
                    if(updateRequest.getNewTransactionLimit() > 0.0){
                        account.setTransactionLimit(updateRequest.getNewTransactionLimit());
                        accountRepository.save(account);
                        return new AccountDto(
                                userEmail,
                                account.getId(),
                                account.getCurrency().name(),
                                account.getTransactionLimit(),
                                account.getBalance(),
                                account.getCreationDate()
                        );
                    }
                }
            }
        }
        return null;
    }


    public List<TransactionDto> getHistory(List<Transaction> transactions,Long accountId){
        List<TransactionDto> transactionsDto = new ArrayList<>();
        for(Transaction transaction:transactions){
            TransactionDto transactionDto=new TransactionDto(
                    accountId,
                    transaction.getAccount().getCurrency().name(),
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getType().name(),
                    transaction.getDescription(),
                    transaction.getTransactionDate()
            );
            transactionsDto.add(transactionDto);
            return transactionsDto;
        }
        return null;
    }

    public List<FixedTermDepositDto> getFixedTerms(List<FixedTermDeposit> fixedTermDeposits,Long accountId){
        List<FixedTermDepositDto> fixedTermDepositsDto = new ArrayList<>();
        for(FixedTermDeposit fixedTermDeposit:fixedTermDeposits){
            FixedTermDepositDto fixedTermDepositDto=new FixedTermDepositDto(
                    accountId,
                    fixedTermDeposit.getId(),
                    fixedTermDeposit.getAmount(),
                    fixedTermDeposit.getInterest(),
                    fixedTermDeposit.getCreationDate(),
                    fixedTermDeposit.getClosingDate()
            );
            fixedTermDepositsDto.add(fixedTermDepositDto);
            return fixedTermDepositsDto;
        }
        return null;
    }



    @Override
    public AccountDto createAccount(String currency, String token) {
        String userEmail = jwtService.extractUsername(token.substring(7));
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            List<Account> userAccounts = user.getAccounts();
            Optional<Account> accountOptional = userAccounts.stream()
                    .filter(account -> account.getCurrency().name().equals(currency))
                    .findFirst();
            if(accountOptional.isEmpty()) {
                Account newAccount = new Account();
                newAccount.setCurrency(ECurrency.valueOf(currency));
                newAccount.setTransactionLimit(currency.equals(ECurrency.ARS.name()) ? 300000.0 : 1000.0);
                newAccount.setBalance(0.0);
                newAccount.setUser(user);
                Account accountCreated = accountRepository.save(newAccount);
                return new AccountDto(
                        userEmail,
                        accountCreated.getId(),
                        currency,
                        accountCreated.getTransactionLimit(),
                        accountCreated.getBalance(),
                        accountCreated.getCreationDate()
                );
            }
        }
        return null;
    }

}


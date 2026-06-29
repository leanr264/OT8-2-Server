package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.request.SimulateFixedTermDepositRequestDto;
import com.alkemy.wallet.dto.response.SimulateFixedTermDepositResponseDto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService{
    @Override
    public SimulateFixedTermDepositResponseDto simulateFixedTermDeposit(SimulateFixedTermDepositRequestDto fixedTermRequest) {
        double amount = fixedTermRequest.getAmount();
        int days = fixedTermRequest.getDays();
        if(amount > 0.0 && days > 0){
            return calculateFixedTermDeposit(amount,days);
        }
        return null;
    }

    private SimulateFixedTermDepositResponseDto calculateFixedTermDeposit(Double amount, int days) {
        double interestRate = 0.002;
        double interestPerDay =  amount * interestRate;
        double totalInterest = interestPerDay * days;
        double totalValue = amount + totalInterest;
        Timestamp creationDate = new Timestamp(new Date().getTime());
        Timestamp closingDate = new Timestamp(new Date().getTime() + (long) days * 24 * 60 * 60 * 1000);
        return new SimulateFixedTermDepositResponseDto(
                amount,
                creationDate,
                closingDate,
                totalInterest,
                totalValue
        );
    }
}

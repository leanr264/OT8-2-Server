package com.alkemy.wallet.service;

import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceImpl implements IExchangeService{
    private final IDollarService dollarService;

    public ExchangeServiceImpl(IDollarService dollarService) {
        this.dollarService = dollarService;
    }

    @Override
    public double convertArsToUsd(double arsAmount){
        double price = dollarService.getDollarPrice();
        return arsAmount / price;
    }

    @Override
    public double convertUsdToArs(double usdAmount){
        double price = dollarService.getDollarPrice();
        return usdAmount * price;
    }
}

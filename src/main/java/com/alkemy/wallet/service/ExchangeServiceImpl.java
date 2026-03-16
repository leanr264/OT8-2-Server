package com.alkemy.wallet.service;

import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceImpl {
    private final IDollarService dollarService;

    public ExchangeServiceImpl(IDollarService dollarService) {
        this.dollarService = dollarService;
    }

    public double convertArsToUsd(double arsAmount){
        double price = dollarService.getDollarPrice();
        return arsAmount / price;
    }

    public double convertUsdToArs(double usdAmount){
        double price = dollarService.getDollarPrice();
        return usdAmount * price;
    }
}

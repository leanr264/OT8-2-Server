package com.alkemy.wallet.service;

import org.springframework.stereotype.Service;

@Service
public class DollarServiceImpl implements IDollarService{
    private static final double DOLLAR_PRICE = 1400.0;

    public double getDollarPrice(){
        return DOLLAR_PRICE;
    }
}

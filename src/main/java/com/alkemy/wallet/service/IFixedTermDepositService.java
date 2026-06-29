package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.request.SimulateFixedTermDepositRequestDto;
import com.alkemy.wallet.dto.response.SimulateFixedTermDepositResponseDto;

public interface IFixedTermDepositService {
    SimulateFixedTermDepositResponseDto simulateFixedTermDeposit(SimulateFixedTermDepositRequestDto fixedTermRequest);
}

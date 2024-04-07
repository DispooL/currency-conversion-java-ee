package com.conversioncurrency.responseDtos;

import java.math.BigDecimal;

public class GetExchangeRateResponseDto {
    private final int id;
    private final GetCurrencyResponseDto baseCurrency;
    private final GetCurrencyResponseDto targetCurrency;
    private final BigDecimal rate;

    public GetExchangeRateResponseDto(int id, GetCurrencyResponseDto baseCurrency, GetCurrencyResponseDto targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public GetCurrencyResponseDto getBaseCurrency() {
        return baseCurrency;
    }

    public GetCurrencyResponseDto getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }
}

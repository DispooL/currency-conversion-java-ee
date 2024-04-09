package com.conversioncurrency.requestDtos;

import com.conversioncurrency.models.Currency;

public class CreateExchangeRateRequest {
    private final Currency baseCurrencyCode;
    private final Currency targetCurrencyCode;
    private final int rate;

    public CreateExchangeRateRequest(Currency baseCurrencyCode, Currency targetCurrencyCode, int rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public Currency getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public Currency getTargetCurrencyCode() {
        return targetCurrencyCode;
    }
    public int getRate() { return rate; }
}

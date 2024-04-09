package com.conversioncurrency.responseDtos;

import java.math.BigDecimal;

public class GetCalculatedExchangeRateResponse {
    private final GetCurrencyResponseDto baseCurrency;
    private final GetCurrencyResponseDto targetCurrency;
    private final BigDecimal rate;
    private final BigDecimal amount;
    private final BigDecimal convertedAmount;

    public GetCalculatedExchangeRateResponse(GetCurrencyResponseDto baseCurrency, GetCurrencyResponseDto targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }
}

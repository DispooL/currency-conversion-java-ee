package com.conversioncurrency.requestDtos;

import java.math.BigDecimal;

public class GetCalculatedExchangeRateRequestDto {
    private final String baseCurrencyCode;
    private final String targetCurrencyCode;
    private final BigDecimal amount;

    public GetCalculatedExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.amount = amount;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}

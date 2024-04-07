package com.conversioncurrency.requestDtos;

import java.math.BigDecimal;

public class UpdateExchangeRateDto {
    private final String pairCode;
    private final BigDecimal rate;

    public UpdateExchangeRateDto(String pairCode, BigDecimal rate) {
        this.pairCode = pairCode;
        this.rate = rate;
    }

    public String getPairCode() {
        return pairCode;
    }

    public BigDecimal getRate() {
        return rate;
    }
}

package com.conversioncurrency.requestDtos;

import java.math.BigDecimal;

public class UpdateExchangeRateRequest {
    private final String pairCode;
    private final BigDecimal rate;

    public UpdateExchangeRateRequest(String pairCode, BigDecimal rate) {
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

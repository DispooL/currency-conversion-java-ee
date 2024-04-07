package com.conversioncurrency.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtils {
    public static BigDecimal centsToDecimal(int cents) {
        return BigDecimal.valueOf(cents)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
    }

    public static int decimalToCents(BigDecimal amountDecimal) {
        return amountDecimal.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_EVEN).intValueExact();
    }

    public static BigDecimal userInputAmountStringToDecimal(String amount) {
        return new BigDecimal(amount.trim());
    }
}

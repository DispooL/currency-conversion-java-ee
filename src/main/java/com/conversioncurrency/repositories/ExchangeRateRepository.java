package com.conversioncurrency.repositories;

import com.conversioncurrency.models.Currency;
import com.conversioncurrency.models.ExchangeRate;
import com.conversioncurrency.requestDtos.CreateExchangeRateRequestDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository {
    List<ExchangeRate> getAll() throws SQLException;
    Optional<ExchangeRate> getByCurrencyPair(String firstCode, String secondCode) throws SQLException;
    Optional<ExchangeRate> create(Currency baseCurrency, Currency targetCurrency, int rate) throws SQLException;
    Optional<ExchangeRate> updateRate(ExchangeRate exchangeRate, int rate) throws SQLException;
}

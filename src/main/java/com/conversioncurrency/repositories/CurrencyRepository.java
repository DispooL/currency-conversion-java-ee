package com.conversioncurrency.repositories;

import com.conversioncurrency.models.Currency;
import com.conversioncurrency.requestDtos.CreateCurrencyRequestDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CurrencyRepository {
    List<Currency> getAll() throws SQLException;
    Optional<Currency> getByCode(String code) throws SQLException;
    Optional<Currency> create(CreateCurrencyRequestDto createCurrencyRequestDto) throws SQLException;
}

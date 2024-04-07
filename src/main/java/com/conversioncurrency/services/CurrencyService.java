package com.conversioncurrency.services;

import com.conversioncurrency.exceptions.CurrencyAlreadyExistsException;
import com.conversioncurrency.models.Currency;
import com.conversioncurrency.repositories.CurrencyRepository;
import com.conversioncurrency.requestDtos.CreateCurrencyRequestDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Optional<Currency> getCurrencyByCode(String code) throws SQLException {
        return this.currencyRepository.getByCode(code);
    }

    public List<Currency> getAllCurrencies() throws SQLException {
        return this.currencyRepository.getAll();
    }

    public Optional<Currency> addCurrency(CreateCurrencyRequestDto createCurrencyRequestDto) throws SQLException, CurrencyAlreadyExistsException {
        Optional<Currency> existingCurrency = this.currencyRepository.getByCode(createCurrencyRequestDto.getCode());
        if (existingCurrency.isPresent()) {
            throw new CurrencyAlreadyExistsException();
        }

        return this.currencyRepository.create(createCurrencyRequestDto);
    }
}

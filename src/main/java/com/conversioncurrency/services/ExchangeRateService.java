package com.conversioncurrency.services;

import com.conversioncurrency.exceptions.CurrencyDoesntExistsException;
import com.conversioncurrency.exceptions.ExchangeRateDoesntExistsException;
import com.conversioncurrency.models.Currency;
import com.conversioncurrency.models.ExchangeRate;
import com.conversioncurrency.repositories.CurrencyRepository;
import com.conversioncurrency.repositories.ExchangeRateRepository;
import com.conversioncurrency.requestDtos.CreateExchangeRateDto;
import com.conversioncurrency.requestDtos.GetCalculatedExchangeRateRequestDto;
import com.conversioncurrency.requestDtos.UpdateExchangeRateDto;
import com.conversioncurrency.responseDtos.CalculatedExchangeRateDto;
import com.conversioncurrency.utils.CurrencyUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateService(CurrencyRepository currencyRepository, ExchangeRateRepository exchangeRateRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        return this.exchangeRateRepository.getAll();
    }

    public Optional<ExchangeRate> getExchangeRateByPair(String pairCode) throws SQLException {
        // Split pair code by two currency pairs
        String[] results = pairCode.split("(?<=\\G.{" + 3 + "})");

        return this.exchangeRateRepository.getByCurrencyPair(results[0], results[1]);
    }

    public Optional<ExchangeRate> createExchangeRate(CreateExchangeRateDto createExchangeRateDto) throws SQLException, CurrencyDoesntExistsException {
        Optional<ExchangeRate> exchangeRate = this.getExchangeRateByPair(createExchangeRateDto.getBaseCurrencyCode().concat(createExchangeRateDto.getTargetCurrencyCode()));
        if (exchangeRate.isPresent()) {
            return exchangeRate;
        }

        Optional<Currency> baseCurrency = this.currencyRepository.getByCode(createExchangeRateDto.getBaseCurrencyCode());
        Optional<Currency> targetCurrency = this.currencyRepository.getByCode(createExchangeRateDto.getTargetCurrencyCode());
        int rate = CurrencyUtils.decimalToCents(createExchangeRateDto.getRate());
        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new CurrencyDoesntExistsException();
        }

        return this.exchangeRateRepository.create(baseCurrency.get(), targetCurrency.get(), rate);
    }

    public Optional<ExchangeRate> updateExchangeRate(UpdateExchangeRateDto updateExchangeRateDto) throws SQLException, ExchangeRateDoesntExistsException {
        Optional<ExchangeRate> exchangeRate = this.getExchangeRateByPair(updateExchangeRateDto.getPairCode());
        if (exchangeRate.isEmpty()) {
            throw new ExchangeRateDoesntExistsException();
        }

        return this.exchangeRateRepository.updateRate(exchangeRate.get(), CurrencyUtils.decimalToCents(updateExchangeRateDto.getRate()));
    }

    public CalculatedExchangeRateDto calculateExchangeRate(GetCalculatedExchangeRateRequestDto exchangeRateRequestDto) throws SQLException, ExchangeRateDoesntExistsException {
        BigDecimal rate;
        Optional<ExchangeRate> rateByPair = this.getExchangeRateByPair(exchangeRateRequestDto.getBaseCurrencyCode().concat(exchangeRateRequestDto.getTargetCurrencyCode()));

        if (rateByPair.isEmpty()) {
            rateByPair = this.getExchangeRateByPair(exchangeRateRequestDto.getTargetCurrencyCode().concat(exchangeRateRequestDto.getBaseCurrencyCode()));
            if (rateByPair.isEmpty()) {
                throw new ExchangeRateDoesntExistsException();
            }
            rate = BigDecimal.ONE.divide(CurrencyUtils.centsToDecimal(rateByPair.get().getRate()), 2, RoundingMode.HALF_EVEN);
        } else {
            rate = CurrencyUtils.centsToDecimal(rateByPair.get().getRate());
        }

        // Calculating converted amount
        BigDecimal convertedAmount = rate.multiply(exchangeRateRequestDto.getAmount());

        return new CalculatedExchangeRateDto(
                rateByPair.get().getBaseCurrency(),
                rateByPair.get().getTargetCurrency(),
                rate,
                exchangeRateRequestDto.getAmount(),
                convertedAmount
        );
    }
}

package com.conversioncurrency.repositories;

import com.conversioncurrency.models.Currency;
import com.conversioncurrency.models.ExchangeRate;
import com.conversioncurrency.requestDtos.CreateExchangeRateRequestDto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateRepository implements ExchangeRateRepository{
    @Override
    public List<ExchangeRate> getAll() throws SQLException {
        String SQL_QUERY = "SELECT e.id, cb.id AS 'baseCurrency_id', cb.name AS 'baseCurrency_name', cb.code AS 'baseCurrency_code', cb.sign AS 'baseCurrency_sign', cb.created_at AS 'baseCurrency_created_at', ct.id AS 'targetCurrency_id', ct.name AS 'targetCurrency_name', ct.code AS 'targetCurrency_code', ct.sign AS 'targetCurrency_sign', ct.created_at as 'targetCurrency_created_at', e.rate FROM exchange_rates e JOIN currencies cb ON e.base_currency_id = cb.id JOIN currencies ct ON e.target_currency_id = ct.id;";
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection conn = DatabaseConnectionFactory.getConnectionFactory().getConnection();
             PreparedStatement pst = conn.prepareStatement(SQL_QUERY);
             ResultSet resultSet = pst.executeQuery()) {
            while(resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                Currency baseCurrency = new Currency();
                baseCurrency.setId(resultSet.getInt("baseCurrency_id"));
                baseCurrency.setCode(resultSet.getString("baseCurrency_code"));
                baseCurrency.setName(resultSet.getString("baseCurrency_name"));
                baseCurrency.setSign(resultSet.getString("baseCurrency_sign"));
                baseCurrency.setCreatedAt(resultSet.getObject("baseCurrency_created_at", LocalDateTime.class));

                Currency targetCurrency = new Currency();
                targetCurrency.setId(resultSet.getInt("targetCurrency_id"));
                targetCurrency.setCode(resultSet.getString("targetCurrency_code"));
                targetCurrency.setName(resultSet.getString("targetCurrency_name"));
                targetCurrency.setSign(resultSet.getString("targetCurrency_sign"));
                targetCurrency.setCreatedAt(resultSet.getObject("targetCurrency_created_at", LocalDateTime.class));

                exchangeRate.setId(resultSet.getInt("id"));
                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);
                exchangeRate.setRate(resultSet.getInt("rate"));

                exchangeRates.add(exchangeRate);
            }
        }

        return exchangeRates;
    }

    @Override
    public Optional<ExchangeRate> getByCurrencyPair(String firstCode, String secondCode) throws SQLException {
        String SQL_QUERY = "SELECT e.id, cb.id AS 'baseCurrency_id', cb.name AS 'baseCurrency_name', cb.code AS 'baseCurrency_code', cb.sign AS 'baseCurrency_sign', cb.created_at AS 'baseCurrency_created_at', ct.id AS 'targetCurrency_id', ct.name AS 'targetCurrency_name', ct.code AS 'targetCurrency_code', ct.sign AS 'targetCurrency_sign', ct.created_at AS 'targetCurrency_created_at', e.rate FROM exchange_rates e JOIN currencies cb ON e.base_currency_id = cb.id JOIN currencies ct ON e.target_currency_id = ct.id WHERE cb.code = ? AND ct.code = ?;";
        Optional<ExchangeRate> resultExchangeRate = Optional.empty();

        try (Connection conn = DatabaseConnectionFactory.getConnectionFactory().getConnection()) {
            PreparedStatement pst = conn.prepareStatement(SQL_QUERY);
            pst.setString(1, firstCode);
            pst.setString(2, secondCode);
            pst.setMaxRows(1);

            ResultSet resultSet = pst.executeQuery();

            if(resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                Currency baseCurrency = new Currency();
                baseCurrency.setId(resultSet.getInt("baseCurrency_id"));
                baseCurrency.setCode(resultSet.getString("baseCurrency_code"));
                baseCurrency.setName(resultSet.getString("baseCurrency_name"));
                baseCurrency.setSign(resultSet.getString("baseCurrency_sign"));
                baseCurrency.setCreatedAt(resultSet.getObject("baseCurrency_created_at", LocalDateTime.class));

                Currency targetCurrency = new Currency();
                targetCurrency.setId(resultSet.getInt("targetCurrency_id"));
                targetCurrency.setCode(resultSet.getString("targetCurrency_code"));
                targetCurrency.setName(resultSet.getString("targetCurrency_name"));
                targetCurrency.setSign(resultSet.getString("targetCurrency_sign"));
                targetCurrency.setCreatedAt(resultSet.getObject("targetCurrency_created_at", LocalDateTime.class));

                exchangeRate.setId(resultSet.getInt("id"));
                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);
                exchangeRate.setRate(resultSet.getInt("rate"));

                resultExchangeRate = Optional.of(exchangeRate);
            }
        }

        return resultExchangeRate;
    }

    @Override
    public Optional<ExchangeRate> create(Currency baseCurrency, Currency targetCurrency, int rate) throws SQLException {
        String SQL_QUERY = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
        Optional<ExchangeRate> resultExchangeRate = Optional.empty();

        try (Connection conn = DatabaseConnectionFactory.getConnectionFactory().getConnection()) {
            PreparedStatement pst = conn.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, baseCurrency.getId());
            pst.setInt(2, targetCurrency.getId());
            pst.setInt(3, rate);
            pst.execute();

            ResultSet resultSet = pst.getGeneratedKeys();

            if (resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setId(resultSet.getInt(1));
                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);
                exchangeRate.setRate(rate);

                resultExchangeRate = Optional.of(exchangeRate);
            }
        }

        return resultExchangeRate;
    }

    @Override
    public Optional<ExchangeRate> updateRate(ExchangeRate exchangeRate, int rate) throws SQLException {
        String SQL_QUERY = "UPDATE exchange_rates SET rate = ? where id = ?;";
        Optional<ExchangeRate> resultExchangeRate = Optional.empty();

        try (Connection conn = DatabaseConnectionFactory.getConnectionFactory().getConnection()) {
            PreparedStatement pst = conn.prepareStatement(SQL_QUERY);
            pst.setInt(1, rate);
            pst.setInt(2, exchangeRate.getId());
            pst.setMaxRows(1);

            int rows = pst.executeUpdate();

            if(rows > 0) {
                exchangeRate.setRate(rate);

                resultExchangeRate = Optional.of(exchangeRate);
            }
        }

        return resultExchangeRate;
    }
}

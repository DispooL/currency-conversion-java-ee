package com.conversioncurrency.repositories;

import com.conversioncurrency.models.Currency;
import com.conversioncurrency.requestDtos.CreateCurrencyRequestDto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCurrencyRepository implements CurrencyRepository {

    @Override
    public List<Currency> getAll() throws SQLException {
        String SQL_QUERY = "select * from currencies";
        List<Currency> currencies = new ArrayList<>();

        try (Connection conn = DatabaseConnectionFactory.getConnectionFactory().getConnection();
             PreparedStatement pst = conn.prepareStatement(SQL_QUERY);
             ResultSet resultSet = pst.executeQuery()) {
            while(resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setName(resultSet.getString("name"));
                currency.setSign(resultSet.getString("sign"));
                currency.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));

                currencies.add(currency);
            }
        }

        return currencies;
    }

    @Override
    public Optional<Currency> getByCode(String code) throws SQLException {
        String SQL_QUERY = "select * from currencies where code = ?";
        Optional<Currency> resultCurrency = Optional.empty();

        try (Connection conn = DatabaseConnectionFactory.getConnectionFactory().getConnection()) {
            PreparedStatement pst = conn.prepareStatement(SQL_QUERY);
            pst.setString(1, code);
            pst.setMaxRows(1);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setName(resultSet.getString("name"));
                currency.setSign(resultSet.getString("sign"));
                currency.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));

                resultCurrency = Optional.of(currency);
            }
        }

        return resultCurrency;
    }

    @Override
    public Optional<Currency> create(CreateCurrencyRequestDto createCurrencyRequestDto) throws SQLException {
        String SQL_QUERY = "insert into currencies (code, name, sign) values(?, ?, ?)";
        Optional<Currency> resultCurrency = Optional.empty();

        try (Connection conn = DatabaseConnectionFactory.getConnectionFactory().getConnection()) {
            PreparedStatement pst = conn.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, createCurrencyRequestDto.getCode());
            pst.setString(2, createCurrencyRequestDto.getName());
            pst.setString(3, createCurrencyRequestDto.getSign());
            pst.execute();

            ResultSet resultSet = pst.getGeneratedKeys();
            if (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt(1));
                currency.setSign(createCurrencyRequestDto.getSign());
                currency.setName(createCurrencyRequestDto.getName());
                currency.setCode(createCurrencyRequestDto.getCode());
                currency.setCreatedAt(LocalDateTime.now());

                resultCurrency = Optional.of(currency);
            }
        }

        return resultCurrency;
    }
}

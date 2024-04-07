package com.conversioncurrency.controllers.currency;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.conversioncurrency.mappers.CurrencyMapper;
import com.conversioncurrency.repositories.JdbcCurrencyRepository;
import com.conversioncurrency.responseDtos.GetCurrencyResponseDto;
import com.conversioncurrency.services.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "GetAllCurrencyServlet", value = "/currencies")
public class GetAllCurrenciesServlet extends HttpServlet {
    private CurrencyService currencyService;

    public void init() {
        this.currencyService = new CurrencyService(new JdbcCurrencyRepository());
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        List<GetCurrencyResponseDto> currencies;
        try {
            currencies = this.currencyService.getAllCurrencies()
                    .stream()
                    .map(CurrencyMapper.MAPPER::currencyToGetCurrencyResponse)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        String jsonResponse = new Gson().toJson(currencies);
        response.getWriter().print(jsonResponse);
    }

    public void destroy() {
    }
}
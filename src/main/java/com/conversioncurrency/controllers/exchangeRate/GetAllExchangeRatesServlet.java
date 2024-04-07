package com.conversioncurrency.controllers.exchangeRate;

import com.conversioncurrency.mappers.ExchangeRateMapper;
import com.conversioncurrency.repositories.JdbcCurrencyRepository;
import com.conversioncurrency.repositories.JdbcExchangeRateRepository;
import com.conversioncurrency.responseDtos.GetExchangeRateResponseDto;
import com.conversioncurrency.services.ExchangeRateService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "GetAllExchangeRatesServlet", value = "/exchangeRates")
public class GetAllExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;

    public void init() {
        this.exchangeRateService = new ExchangeRateService(new JdbcCurrencyRepository(), new JdbcExchangeRateRepository());
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        List<GetExchangeRateResponseDto> exchangeRates;
        try {
            exchangeRates = this.exchangeRateService.getAllExchangeRates()
                    .stream()
                    .map(ExchangeRateMapper.MAPPER::exchangeRateToResponse)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        String jsonResponse = new Gson().toJson(exchangeRates);
        response.getWriter().print(jsonResponse);
    }

    public void destroy() {
    }
}
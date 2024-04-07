package com.conversioncurrency.controllers.exchangeRate;

import com.conversioncurrency.mappers.ExchangeRateMapper;
import com.conversioncurrency.models.ExchangeRate;
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
import java.util.Optional;

@WebServlet(name = "GetSingleExchangeRateServlet", value = "/exchangeRate/*")
public class GetSingleExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;

    public void init() {
        this.exchangeRateService = new ExchangeRateService(new JdbcCurrencyRepository(), new JdbcExchangeRateRepository());
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        // Return 400 if currency pair is not valid
        if (!this.isCurrencyPairValid(pathParts[1])) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.setContentType("application/json");
        Optional<ExchangeRate> exchangeRate;
        try {
            exchangeRate = this.exchangeRateService.getExchangeRateByPair(pathParts[1]);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Return 404 if exchange rate doesn't exist
        if (exchangeRate.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        GetExchangeRateResponseDto responseDto = ExchangeRateMapper.MAPPER.exchangeRateToResponse(exchangeRate.get());
        String jsonResponse = new Gson().toJson(responseDto);

        response.getWriter().print(jsonResponse);
        response.getWriter().flush();
    }

    public void destroy() {
    }

    // Check that only 6 character strings allowed which don't contain any digits
    private Boolean isCurrencyPairValid(String currencyPair) {
        return currencyPair.length() == 6 && currencyPair.chars().noneMatch(Character::isDigit);
    }
}
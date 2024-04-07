package com.conversioncurrency.controllers.exchangeRate;

import com.conversioncurrency.exceptions.CurrencyDoesntExistsException;
import com.conversioncurrency.mappers.ExchangeRateMapper;
import com.conversioncurrency.models.ExchangeRate;
import com.conversioncurrency.repositories.JdbcCurrencyRepository;
import com.conversioncurrency.repositories.JdbcExchangeRateRepository;
import com.conversioncurrency.requestDtos.CreateExchangeRateDto;
import com.conversioncurrency.responseDtos.GetExchangeRateResponseDto;
import com.conversioncurrency.services.ExchangeRateService;
import com.conversioncurrency.utils.CurrencyUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "AddExchangeRateServlet", value = "/exchangeRate/add")
public class AddExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;

    public void init() {
        this.exchangeRateService = new ExchangeRateService(new JdbcCurrencyRepository(), new JdbcExchangeRateRepository());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        // Return 400 if currency pairs or rate are not valid
        if (!this.areCurrencyPairsAndRateValid(baseCurrencyCode, targetCurrencyCode, rate)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CreateExchangeRateDto createExchangeRateDto = new CreateExchangeRateDto(
                baseCurrencyCode,
                targetCurrencyCode,
                CurrencyUtils.userInputAmountStringToDecimal(rate)
        );

        Optional<ExchangeRate> exchangeRate;
        try {
            exchangeRate = this.exchangeRateService.createExchangeRate(createExchangeRateDto);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (CurrencyDoesntExistsException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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

    private Boolean areCurrencyPairsAndRateValid(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
            return false;
        }

        if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            return false;
        }

        try{
            new BigDecimal(rate);
            return true;
        } catch (NumberFormatException ignored) {
        }

        return false;
    }
}
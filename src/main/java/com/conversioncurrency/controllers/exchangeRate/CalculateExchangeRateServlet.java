package com.conversioncurrency.controllers.exchangeRate;

import com.conversioncurrency.exceptions.ExchangeRateDoesntExistsException;
import com.conversioncurrency.mappers.ExchangeRateMapper;
import com.conversioncurrency.repositories.JdbcCurrencyRepository;
import com.conversioncurrency.repositories.JdbcExchangeRateRepository;
import com.conversioncurrency.requestDtos.GetCalculatedExchangeRateRequestDto;
import com.conversioncurrency.responseDtos.CalculatedExchangeRateDto;
import com.conversioncurrency.responseDtos.GetCalculatedExchangeRateResponseDto;
import com.conversioncurrency.services.ExchangeRateService;
import com.conversioncurrency.utils.CurrencyUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet(name = "CalculateExchangeRateServlet", value = "/exchange")
public class CalculateExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;

    public void init() {
        this.exchangeRateService = new ExchangeRateService(new JdbcCurrencyRepository(), new JdbcExchangeRateRepository());
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        CalculatedExchangeRateDto calculatedRate;

        if (!this.areParamsValid(request.getParameter("from"), request.getParameter("to"), request.getParameter("amount"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GetCalculatedExchangeRateRequestDto exchangeRateRequestDto = new GetCalculatedExchangeRateRequestDto(
                request.getParameter("from"),
                request.getParameter("to"),
                CurrencyUtils.userInputAmountStringToDecimal(request.getParameter("amount"))
        );

        try {
            calculatedRate = this.exchangeRateService.calculateExchangeRate(exchangeRateRequestDto);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (ExchangeRateDoesntExistsException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        GetCalculatedExchangeRateResponseDto responseDto = ExchangeRateMapper.MAPPER.calculatedRateToResponse(calculatedRate);

        String jsonResponse = new Gson().toJson(responseDto);
        response.getWriter().print(jsonResponse);
    }

    public void destroy() {
    }

    private boolean areParamsValid(String baseCurrencyCode, String targetCurrencyCode, String amount) {
        if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3 || amount == null) {
            return false;
        }

        try {
            new BigDecimal(amount.trim());
            return true;
        } catch (NumberFormatException ignored) {
        }

        return false;
    }
}
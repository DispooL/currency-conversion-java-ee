package com.conversioncurrency.controllers.exchangeRate;

import com.conversioncurrency.exceptions.ExchangeRateDoesntExistsException;
import com.conversioncurrency.mappers.ExchangeRateMapper;
import com.conversioncurrency.models.ExchangeRate;
import com.conversioncurrency.repositories.JdbcCurrencyRepository;
import com.conversioncurrency.repositories.JdbcExchangeRateRepository;
import com.conversioncurrency.requestDtos.UpdateExchangeRateDto;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "UpdateExchangeRateServlet", value = "/exchangeRate/update")
public class UpdateExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;

    public void init() {
        this.exchangeRateService = new ExchangeRateService(new JdbcCurrencyRepository(), new JdbcExchangeRateRepository());
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        if (!method.equals("PATCH")) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        this.doPatch(request, response);
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Map<String, String> formParams = parseFormEncodedData(requestBody);

        // Return 400 if currency pair or rate is not valid
        if (!this.areCurrencyPairAndRateValid(formParams.get("pair"), formParams.get("rate"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UpdateExchangeRateDto updateExchangeRateDto = new UpdateExchangeRateDto(
                formParams.get("pair"),
                CurrencyUtils.userInputAmountStringToDecimal(formParams.get("rate"))
        );

        Optional<ExchangeRate> exchangeRate;
        try {
            exchangeRate = this.exchangeRateService.updateExchangeRate(updateExchangeRateDto);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (ExchangeRateDoesntExistsException e) {
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

    private Boolean areCurrencyPairAndRateValid(String pairCode, String rate) {
        if (pairCode == null || rate == null) {
            return false;
        }

        boolean isCurrencyPairValid = pairCode.length() == 6 && pairCode.chars().noneMatch(Character::isDigit);
        boolean isRateValid = false;

        try{
            Integer.parseInt(rate);
            isRateValid = true;
        } catch (NumberFormatException ignored) {
        }

        return isCurrencyPairValid && isRateValid;
    }

    private Map<String, String> parseFormEncodedData(String data) {
        Map<String, String> params = new HashMap<>();
        for (String param : data.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length > 1) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
}
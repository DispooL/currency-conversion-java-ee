package com.conversioncurrency.controllers.currency;

import com.conversioncurrency.mappers.CurrencyMapper;
import com.conversioncurrency.models.Currency;
import com.conversioncurrency.repositories.JdbcCurrencyRepository;
import com.conversioncurrency.responseDtos.GetCurrencyResponseDto;
import com.conversioncurrency.services.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "GetCurrencyServlet", value = "/currencies/*")
public class GetSingleCurrencyServlet extends HttpServlet {
    private CurrencyService currencyService;

    public void init() {
        this.currencyService = new CurrencyService(new JdbcCurrencyRepository());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        response.setContentType("application/json");
        Optional<Currency> currency;
        try {
            currency = this.currencyService.getCurrencyByCode(pathParts[1]);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Return 404 if currency doesn't exist
        if (currency.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        GetCurrencyResponseDto responseDto = CurrencyMapper.MAPPER.currencyToGetCurrencyResponse(currency.get());
        String jsonResponse = new Gson().toJson(responseDto);

        response.getWriter().print(jsonResponse);
        response.getWriter().flush();
    }

    public void destroy() {
    }
}
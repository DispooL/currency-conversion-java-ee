package com.conversioncurrency.controllers.currency;

import com.conversioncurrency.exceptions.CurrencyAlreadyExistsException;
import com.conversioncurrency.mappers.CurrencyMapper;
import com.conversioncurrency.models.Currency;
import com.conversioncurrency.repositories.JdbcCurrencyRepository;
import com.conversioncurrency.requestDtos.CreateCurrencyRequestDto;
import com.conversioncurrency.responseDtos.GetCurrencyResponseDto;
import com.conversioncurrency.services.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "AddCurrencyServlet", value = "/currencies/add")
public class AddCurrencyServlet extends HttpServlet {
    private CurrencyService currencyService;

    public void init() {
        this.currencyService = new CurrencyService(new JdbcCurrencyRepository());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        // Return 400 if parameters are not valid
        if (!this.areParamsValidForCurrencyCreation(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CreateCurrencyRequestDto createCurrencyRequestDto = new CreateCurrencyRequestDto(
                request.getParameter("code"),
                request.getParameter("name"),
                request.getParameter("sign")
        );
        Optional<Currency> createdCurrency;

        try {
            createdCurrency = this.currencyService.addCurrency(createCurrencyRequestDto);
        } catch (SQLException e) {
            // Return 500 on SQL exception
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (CurrencyAlreadyExistsException e) {
            // Return 409 on existing currency
            response.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }

        // Return 500 if currency was not created
        if (createdCurrency.isEmpty()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        GetCurrencyResponseDto currencyResponseDto = CurrencyMapper.MAPPER.currencyToGetCurrencyResponse(createdCurrency.get());
        String jsonResponse = new Gson().toJson(currencyResponseDto);
        response.getWriter().print(jsonResponse);
    }

    public void destroy() {
    }

    private Boolean areParamsValidForCurrencyCreation(HttpServletRequest request) {
        return request.getParameter("code") != null && request.getParameter("name") != null && request.getParameter("sign") != null;
    }
}
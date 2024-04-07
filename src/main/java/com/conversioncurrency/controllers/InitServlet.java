package com.conversioncurrency.controllers;

import com.conversioncurrency.repositories.DatabaseConnectionFactory;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;

@WebServlet(name = "InitServlet", value = "/initServlet", loadOnStartup = 1)
public class InitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public InitServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        try {
            DatabaseConnectionFactory.getConnectionFactory().init();
        }
        catch (IOException e) {
            config.getServletContext().log(e.getLocalizedMessage(),e);
        }
    }
}
package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.entity.dto.ExchangeRateDto;
import org.petproject.service.CurrencyService;
import org.petproject.service.ExchangeRateService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();
        ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
        var jsonString = gson.toJson(exchangeRateService.findAll());
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var gson = new Gson();
        var currencyService = CurrencyService.getInstance();
        var exchangeRateService = ExchangeRateService.getInstance();

        var baseCurrencyCode = req.getParameter("baseCurrencyCode");
        var targetCurrencyCode = req.getParameter("targetCurrencyCode");
        var rate = req.getParameter("rate");

        var exchangeRateDto = new ExchangeRateDto(0,
                currencyService.getByCode(baseCurrencyCode),
                currencyService.getByCode(targetCurrencyCode),
                Double.parseDouble(rate));

        var saved = exchangeRateService.save(exchangeRateDto);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        var jsonString = gson.toJson(saved);
        out.print(jsonString);
        out.flush();
    }
}

package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.ErrorResponse;
import org.petproject.service.CurrencyService;
import org.petproject.service.ExchangeRateService;
import org.petproject.util.DataValidator;
import org.sqlite.SQLiteException;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
            resp.getWriter().write(gson.toJson(exchangeRateService.findAll()));
        } catch (SQLException exception) {
            if (exception.getMessage().equals("ExchangeRateMapper: currency not found")) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(new ErrorResponse("The exchange rate refers to a currency that is not in the database.")));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var currencyService = CurrencyService.getInstance();
        var exchangeRateService = ExchangeRateService.getInstance();

        var baseCurrencyCode = req.getParameter("baseCurrencyCode");
        var targetCurrencyCode = req.getParameter("targetCurrencyCode");
        var rate = req.getParameter("rate");

        if (DataValidator.isCurrencyCodeNotValid(baseCurrencyCode) ||
                DataValidator.isCurrencyCodeNotValid(targetCurrencyCode) ||
                DataValidator.isExchangeRateNotValid(rate)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The required form field is missing or incorrect.")));
            return;
        }
        ExchangeRateDto exchangeRateDto;

        try {
            var optionalBaseCurrencyDto = currencyService.getByCode(baseCurrencyCode);
            var optionalTargetCurrencyDto = currencyService.getByCode(targetCurrencyCode);
            if (optionalBaseCurrencyDto.isPresent() && optionalTargetCurrencyDto.isPresent()) {
                exchangeRateDto = new ExchangeRateDto(0,
                        optionalBaseCurrencyDto.get(),
                        optionalTargetCurrencyDto.get(),
                        Double.parseDouble(rate));
                var saved = exchangeRateService.save(exchangeRateDto);
                if (saved.isPresent()) {
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write(gson.toJson(saved.get()));
                    return;
                }
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(gson.toJson(new ErrorResponse("One (or both) currency from the currency pair does not exist in the database.")));
        } catch (SQLException exception) {
            if (exception.getMessage().equals("org.sqlite.SQLiteException: [SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed (UNIQUE constraint failed: ExchangeRates.BaseCurrencyId, ExchangeRates.TargetCurrencyId)")) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write(gson.toJson(new ErrorResponse("A currency pair with this code already exists.")));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
            }
        }
    }
}


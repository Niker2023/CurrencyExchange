package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.dto.CurrencyDto;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.ErrorResponse;
import org.petproject.service.CurrencyService;
import org.petproject.service.ExchangeRateService;
import org.petproject.util.DataValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
            resp.getWriter().write(gson.toJson(exchangeRateService.findAll()));
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
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

        Optional<ExchangeRateDto> exchangeRateDto = Optional.empty();

        try {
            var optionalBaseCurrencyDto = currencyService.getByCode(baseCurrencyCode);
            var optionalTargetCurrencyDto = currencyService.getByCode(targetCurrencyCode);
            if (optionalBaseCurrencyDto.isPresent() && optionalTargetCurrencyDto.isPresent()) {
                var baseCurrencyDTO = optionalBaseCurrencyDto.get();
                var targetCurrencyDTO = optionalTargetCurrencyDto.get();
                exchangeRateDto = Optional.of(new ExchangeRateDto(0,
                        targetCurrencyDTO,
                        baseCurrencyDTO,
                        Double.parseDouble(rate)));
            } else {
                // exception
            }
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
            return;
        }

        try {
            if (exchangeRateDto.isPresent()) {
                var saved = exchangeRateService.save(exchangeRateDto.get());
            } else {
                // exception
            }

        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
        }
    }
}

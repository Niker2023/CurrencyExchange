package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.dto.ExchangeAmountDto;
import org.petproject.entity.ErrorResponse;
import org.petproject.service.CurrencyService;
import org.petproject.service.ExchangeRateService;
import org.petproject.util.DataValidator;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        var baseCurrencyCode = req.getParameter("from");
        var targetCurrencyCode = req.getParameter("to");
        var amount = req.getParameter("amount");

        if (DataValidator.isCurrencyCodeNotValid(baseCurrencyCode) ||
                DataValidator.isCurrencyCodeNotValid(targetCurrencyCode) ||
                DataValidator.isExchangeRateNotValid(amount)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The required form is missing from the address or is incorrect.")));
            return;
        }
        var exchangeRateService = ExchangeRateService.getInstance();
        var currencyService = CurrencyService.getInstance();
        try {
            var optionalBaseCurrencyDto = currencyService.getByCode(baseCurrencyCode);
            var optionalTargetCurrencyDto = currencyService.getByCode(targetCurrencyCode);

            if (optionalBaseCurrencyDto.isPresent() && optionalTargetCurrencyDto.isPresent()) {
                var exchangeAmountDto = new ExchangeAmountDto(optionalBaseCurrencyDto.get(),
                        optionalTargetCurrencyDto.get(),
                        0, Double.parseDouble(amount), 0);
                var result = exchangeRateService.exchangeAmount(exchangeAmountDto);
                if (result.isPresent()) {
                    resp.getWriter().write(gson.toJson(result.get()));
                    return;
                }
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The exchange rate for the pair was not found.")));
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
        }
    }
}

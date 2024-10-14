package org.petproject.servlet;

import com.google.gson.Gson;
import com.google.common.base.Splitter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.ErrorResponse;
import org.petproject.service.CurrencyService;
import org.petproject.service.ExchangeRateService;
import org.petproject.util.DataValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var codeString = req.getPathInfo().substring(1);

        if (DataValidator.isCurrenciesCodesNotValid(codeString)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The currency codes of the pair are missing in the address.")));
            return;
        }

        String baseCurrency = codeString.substring(0, 3);
        String targetCurrency = codeString.substring(3, 6);

        try {
            var optionalExchangeRateDto = ExchangeRateService.getInstance().findByCodes(baseCurrency, targetCurrency);
            if (optionalExchangeRateDto.isPresent()) {
                resp.getWriter().write(gson.toJson(optionalExchangeRateDto.get()));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ErrorResponse("The exchange rate for the pair was not found.")));
            }
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
        }
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        } else {
            this.doPatch(req, resp);
        }
    }


    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String rate;
        var codeString = req.getPathInfo().substring(1);

        if (!br.ready() || DataValidator.isExchangeRateNotValid(rate = getRateFromBufferedReader(br))
                || DataValidator.isCurrenciesCodesNotValid(codeString)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The required form field is missing.")));
            return;
        }

        String baseCurrency = codeString.substring(0, 3);
        String targetCurrency = codeString.substring(3, 6);

        var currencyService = CurrencyService.getInstance();
        var exchangeRateService = ExchangeRateService.getInstance();

        try {
            var optionalBaseCurrencyDto = currencyService.getByCode(baseCurrency);
            var optionalTargetCurrencyDto = currencyService.getByCode(targetCurrency);
            ExchangeRateDto exchangeRateDto;
            if (optionalBaseCurrencyDto.isPresent() && optionalTargetCurrencyDto.isPresent()) {
                exchangeRateDto = new ExchangeRateDto(optionalBaseCurrencyDto.get(),
                        optionalTargetCurrencyDto.get(),
                        Double.parseDouble(rate));

                var saved = exchangeRateService.update(exchangeRateDto);
                if (saved.isPresent()) {
                    resp.getWriter().write(gson.toJson(saved.get()));
                    return;
                }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The currency pair is missing from the database.")));
            }
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
        }
    }


    private String getRateFromBufferedReader(BufferedReader br) throws IOException {
        Map<String, String> result = Splitter.on("&")
                .withKeyValueSeparator("=")
                .split(br.readLine());
        return result.get("rate");
    }
}

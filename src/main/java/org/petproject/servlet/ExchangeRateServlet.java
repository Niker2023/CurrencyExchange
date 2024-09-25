package org.petproject.servlet;

import com.google.gson.Gson;
import com.google.common.base.Splitter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.service.CurrencyService;
import org.petproject.service.ExchangeRateService;
import org.petproject.util.DataValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var codeString = req.getPathInfo().substring(1);

        if (DataValidator.isCurrenciesCodesNotValid(codeString)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        String baseCurrency = codeString.substring(0, 3);
        String targetCurrency = codeString.substring(3, 6);



        var exchangeRateService = ExchangeRateService.getInstance();
        try {
            var jsonString = gson.toJson(exchangeRateService.findByCodes(baseCurrency, targetCurrency));
        } catch (SQLException e) {

        }
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        }
        this.doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var codeString = req.getPathInfo().substring(1);

        if (codeString.length() != 6) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        String baseCurrency = codeString.substring(0, 3);
        String targetCurrency = codeString.substring(3, 6);

        var currencyService = CurrencyService.getInstance();
        var exchangeRateService = ExchangeRateService.getInstance();

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));

        String line = br.readLine();
        Map<String, String> result = Splitter.on("&")
                .withKeyValueSeparator("=")
                .split(line);

        var rate = result.get("rate");

        try {
            var exchangeRateDto = new ExchangeRateDto(0,
                    currencyService.getByCode(baseCurrency).get(),
                    currencyService.getByCode(targetCurrency).get(),
                    Double.parseDouble(rate));
            var saved = exchangeRateService.update(exchangeRateDto);

        } catch (SQLException e) {
        }

    }
}

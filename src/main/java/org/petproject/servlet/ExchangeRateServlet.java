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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var reqPathInfo = req.getPathInfo();
        var codeString = reqPathInfo.substring(1);

        if (codeString.length() != 6) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        String baseCurrency = codeString.substring(0, 3);
        String targetCurrency = codeString.substring(3, 6);

        Gson gson = new Gson();

        var exchangeRateService = ExchangeRateService.getInstance();
        var jsonString = gson.toJson(exchangeRateService.findByCodes(baseCurrency, targetCurrency));
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
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

        var reqPathInfo = req.getPathInfo();
        var codeString = reqPathInfo.substring(1);

        if (codeString.length() != 6) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        String baseCurrency = codeString.substring(0, 3);
        String targetCurrency = codeString.substring(3, 6);

        var gson = new Gson();
        var currencyService = CurrencyService.getInstance();
        var exchangeRateService = ExchangeRateService.getInstance();

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));

        String line = br.readLine();
        Map<String, String> result = Splitter.on("&")
                .withKeyValueSeparator("=")
                .split(line);

        var rate = result.get("rate");

        var exchangeRateDto = new ExchangeRateDto(0,
                currencyService.getByCode(baseCurrency),
                currencyService.getByCode(targetCurrency),
                Double.parseDouble(rate));

        var saved = exchangeRateService.update(exchangeRateDto);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        var jsonString = gson.toJson(saved);
        out.print(jsonString);
        out.flush();
    }
}

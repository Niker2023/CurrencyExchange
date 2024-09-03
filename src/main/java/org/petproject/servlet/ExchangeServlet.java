package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var baseCurrencyCode = req.getParameter("from");
        var targetCurrencyCode = req.getParameter("to");
        var amount = req.getParameter("amount");

        Gson gson = new Gson();
//        var currencyService = CurrencyService.getInstance();
//        var jsonString = gson.toJson(currencyService.getByCode(codeString));
//        PrintWriter out = resp.getWriter();
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        out.print(jsonString);
//        out.flush();
    }

}

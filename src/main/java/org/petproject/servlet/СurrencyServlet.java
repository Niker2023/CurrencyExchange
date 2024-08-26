package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.model.Currency;
import org.petproject.model.CurrencyDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currencies")
public class СurrencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();
        var currencyDao = new CurrencyDao();
        var jsonString = gson.toJson(currencyDao.getAll());
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var gson = new Gson();
        var currencyDao = new CurrencyDao();

        var name = req.getParameter("name");
        var code = req.getParameter("code");
        var sign = req.getParameter("sign");

        var currency = new Currency(0, name, code, sign);
        var currentCurrency = currencyDao.save(currency);


        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        var jsonString = gson.toJson(currentCurrency);
        out.print(jsonString);
        out.flush();
    }
}

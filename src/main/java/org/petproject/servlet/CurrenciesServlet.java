package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.dto.CurrencyDto;
import org.petproject.entity.ErrorResponse;
import org.petproject.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jsonString = "";
        try {
            var currencyDtoList = CurrencyService.getInstance().findAll();
            jsonString = gson.toJson(currencyDtoList);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonString = gson.toJson(new ErrorResponse("The database is unavailable."));
        } finally {
            resp.getWriter().write(jsonString);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var name = req.getParameter("name");
        var code = req.getParameter("code");
        var sign = req.getParameter("sign");

        var currencyDto = new CurrencyDto(0, name, code, sign);
        var currentCurrency = CurrencyService.getInstance().save(currencyDto);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        var jsonString = gson.toJson(currentCurrency);
        out.print(jsonString);
        out.flush();
    }
}

package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.dto.CurrencyDto;
import org.petproject.entity.ErrorResponse;
import org.petproject.service.CurrencyService;
import org.petproject.util.DataValidator;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            var currencyDtoList = CurrencyService.getInstance().findAll();
            resp.getWriter().write(gson.toJson(currencyDtoList));
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var name = req.getParameter("name");
        var code = req.getParameter("code");
        var sign = req.getParameter("sign");

        if (name == null || name.isBlank() ||
                sign == null || sign.isBlank() ||
                DataValidator.isCurrencyCodeNotValid(code)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The required form field is missing or incorrect.")));
            return;
        }

        var currencyDto = new CurrencyDto(0, name, code, sign);

        try {
            var currentCurrency = CurrencyService.getInstance().save(currencyDto);
            resp.getWriter().write(gson.toJson(currentCurrency));
        } catch (SQLException exception) {
            if (exception.getMessage().equals("[SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed (UNIQUE constraint failed: Currencies.Code)")) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write(gson.toJson(new ErrorResponse("A currency with this code already exists.")));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
            }
        }
    }
}

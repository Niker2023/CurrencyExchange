package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.entity.ErrorResponse;
import org.petproject.service.CurrencyService;
import org.petproject.util.DataValidator;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var codeString = req.getPathInfo().substring(1);

        if (DataValidator.isCurrencyCodeNotValid(codeString)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The currency code is missing from the address or is incorrect.")));
        }

        try {
            var currencyService = CurrencyService.getInstance();
            var currencyByCode = currencyService.getByCode(codeString);
            if (currencyByCode.isPresent()) {
                resp.getWriter().write(gson.toJson(currencyByCode.get()));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ErrorResponse("The currency was not found.")));
            }
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("The database is unavailable.")));
        }
    }
}

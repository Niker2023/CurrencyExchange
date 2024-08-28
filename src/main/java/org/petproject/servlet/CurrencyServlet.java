package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var reqPathInfo = req.getPathInfo();
        var codeString = reqPathInfo.substring(1);

        if (codeString.length() != 3) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        Gson gson = new Gson();
        var currencyService = CurrencyService.getInstance();
        var jsonString = gson.toJson(currencyService.getByCode(codeString));
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }
}

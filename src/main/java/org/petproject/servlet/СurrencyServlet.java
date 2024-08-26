package org.petproject.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.petproject.model.CurrencyDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currencies")
public class СurrencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        try {
//            Class.forName("org.gson");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        Gson gson = new Gson();
        var currencyDao = new CurrencyDao();
        var jsonString = gson.toJson(currencyDao.getAll());
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();

    }
}

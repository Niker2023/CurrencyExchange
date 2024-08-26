package org.petproject.model;

import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao{


    @Override
    public Optional<Currency> get(int id) {
        return Optional.empty();
    }

    @Override
    public List<Currency> getAll() {
        List<Currency> currencyList = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        var pathToDB = "/home/nikita/IdeaProjects/CurrencyExchange/currency.db";
        var pathToDB = "currency.db";
        try
                (
                        // create a database connection
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pathToDB);
                        Statement statement = connection.createStatement();
                )
        {
            ResultSet rs = statement.executeQuery("select * from currencies");
            while(rs.next())
            {
                currencyList.add(new Currency(rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencyList;
    }

    @Override
    public void save(Currency currency) {

    }

    @Override
    public void update(Currency currency, String[] params) {

    }
}

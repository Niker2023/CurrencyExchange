package org.petproject.model;

import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
                currencyList.add(new Currency(rs.getInt("id"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currencyList;
    }

    @Override
    public Currency save(Currency currency) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        var pathToDB = "/home/nikita/IdeaProjects/CurrencyExchange/currency.db";
        var pathToDB = "currency.db";

        int SaveId = 0;

        try
                (
                        // create a database connection
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pathToDB);
                        Statement statement = connection.createStatement();
                )
        {
            statement.executeQuery("""
                                    insert into currencies(Code,FullName,Sign)
                                    """);
            var resultSet = statement.executeQuery("SELECT last_insert_rowid()");
            while(resultSet.next())
            {
                SaveId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return new Currency(SaveId, currency.getName(), currency.getCode(), currency.getSign());



    }

    @Override
    public void update(Currency currency, String[] params) {

    }
}

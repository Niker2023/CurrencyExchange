package org.petproject.dao;

import org.petproject.entity.Currency;
import org.petproject.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao {

    private final String FIND_ALL= """
            SELECT *
            FROM CURRENCIES
            """;

    private final String INSERT_CURRENCY = """
            INSERT INTO CURRENCY(Code, FullName, Sign)
            VALUES (?, ?, ?)
            """;

    private final String FIND_LAST_INSERT = """
            SELECT last_insert_rowid()
            """;


    @Override
    public Optional<Currency> get(int id) {
        return Optional.empty();
    }


    @Override
    public List<Currency> getAll() {
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(FIND_ALL);
                )
        {
            var rs = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while(rs.next())
            {
                currencies.add(new Currency(rs.getInt("id"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign")));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Currency save(Currency currency) {
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(INSERT_CURRENCY);
                )
        {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getCode());
            preparedStatement.setString(3, currency.getCode());
            preparedStatement.executeQuery();

            var rs = connection.prepareStatement(FIND_LAST_INSERT).executeQuery();
            return new Currency(rs.getInt("id"), currency.getCode(), currency.getCode(), currency.getCode());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Currency currency, String[] params) {

    }
}

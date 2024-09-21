package org.petproject.dao;

import org.petproject.entity.Currency;
import org.petproject.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private static final CurrencyDao INSTANCE = new CurrencyDao();


    public Optional<Currency> getByCode(String code) {
        String FIND_BY_CODE = """
                SELECT *
                FROM CURRENCIES
                WHERE Code = ?
                """;
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(FIND_BY_CODE);
                )
        {
            preparedStatement.setString(1, code);
            var resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return Optional.of(new Currency(resultSet.getInt("id"), resultSet.getString("FullName"), resultSet.getString("Code"), resultSet.getString("Sign")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<Currency> getById(int id) {
        String FIND_BY_ID = """
                SELECT *
                FROM CURRENCIES
                WHERE id = ?
                """;
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(FIND_BY_ID);
                )
        {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.of(new Currency(resultSet.getInt("id"),
                    resultSet.getString("FullName"),
                    resultSet.getString("Code"),
                    resultSet.getString("Sign")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private CurrencyDao(){}


    public static CurrencyDao getInstance() {
        return INSTANCE;
    }


    public List<Currency> getAll() {
        String FIND_ALL = """
                SELECT *
                FROM CURRENCIES
                """;
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


    public Currency save(Currency currency) {

        String INSERT_CURRENCY = """
                INSERT INTO CURRENCIES(Code, FullName, Sign)
                VALUES (?, ?, ?)
                """;
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(INSERT_CURRENCY);
                )
        {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();

            String FIND_LAST_INSERT = """
                    SELECT *
                    FROM Currencies
                    ORDER BY id DESC
                    LIMIT 1
                    """;
            var resultSet = connection.prepareStatement(FIND_LAST_INSERT).executeQuery();
            resultSet.next();
            return new Currency(resultSet.getInt("id"), resultSet.getString("FullName"),
                    resultSet.getString("Code"), resultSet.getString("Sign"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

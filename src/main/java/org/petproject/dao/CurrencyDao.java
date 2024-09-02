package org.petproject.dao;

import org.petproject.entity.Currency;
import org.petproject.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private final String FIND_LAST_INSERT = """
                    SELECT *
                    FROM Currencies
                    ORDER BY id DESC
                    LIMIT 1
                    """;

    private final String INSERT_CURRENCY = """
                INSERT INTO CURRENCIES(Code, FullName, Sign)
                VALUES (?, ?, ?)
                """;

    private final String FIND_ALL = """
                SELECT *
                FROM CURRENCIES
                """;

    private final String FIND_BY_CODE = """
            SELECT *
            FROM CURRENCIES
            WHERE Code = ?
            """;

    private final String FIND_BY_ID = """
            SELECT *
            FROM CURRENCIES
            WHERE id = ?
            """;


    public Optional<Currency> getByCode(String code) {
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

            var resultSet = connection.prepareStatement(FIND_LAST_INSERT).executeQuery();
            resultSet.next();
            return new Currency(resultSet.getInt("id"), resultSet.getString("FullName"),
                    resultSet.getString("Code"), resultSet.getString("Sign"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

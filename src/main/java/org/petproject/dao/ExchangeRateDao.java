package org.petproject.dao;

import org.petproject.entity.Currency;
import org.petproject.entity.ExchangeRate;
import org.petproject.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao  {

    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();

    private final String FIND_ALL = """
            SELECT *
            FROM ExchangeRates
            """;

    private final String FIND_BY_CODES = """
            SELECT *
            FROM ExchangeRates
            WHERE BaseCurrencyId = ?
            AND TargetCurrencyId = ?
            """;

    private final String INSERT_EXCHANGE_RATE = """
            INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
            VALUES (?, ?, ?)
            """;

    private final String FIND_LAST_INSERT = """
                    SELECT *
                    FROM ExchangeRates
                    ORDER BY id DESC
                    LIMIT 1
                    """;

    private final String UPDATE_RATE = """
            UPDATE ExchangeRates
            SET Rate = ?
            WHERE BaseCurrencyId = ?
            AND TargetCurrencyId = ?
            """;


    public Optional<ExchangeRate> getByIds(int baseCurrencyId, int targetCurrencyId) {
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(FIND_BY_CODES);
                )
        {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Optional.of(new ExchangeRate(resultSet.getInt("id"),
                    resultSet.getInt("BaseCurrencyId"),
                    resultSet.getInt("TargetCurrencyId"),
                    resultSet.getDouble("Rate")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private ExchangeRateDao() {}


    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }


    public List<ExchangeRate> getAll() {
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(FIND_ALL);
                )
        {
            var resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while(resultSet.next())
            {
                exchangeRates.add(new ExchangeRate(resultSet.getInt("id"),
                        resultSet.getInt("BaseCurrencyId"),
                        resultSet.getInt("TargetCurrencyId"),
                        resultSet.getDouble("Rate")));
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ExchangeRate save(ExchangeRate exchangeRate) {
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(INSERT_EXCHANGE_RATE);
                )
        {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();

            var resultSet = connection.prepareStatement(FIND_LAST_INSERT).executeQuery();
            resultSet.next();
            return new ExchangeRate(resultSet.getInt("id"), resultSet.getInt("BaseCurrencyId"),
                    resultSet.getInt("TargetCurrencyId"), resultSet.getDouble("Rate"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ExchangeRate update(ExchangeRate exchangeRate) {
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(UPDATE_RATE);
                )
        {
            preparedStatement.setDouble(1, exchangeRate.getRate());
            preparedStatement.setInt(2, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(3, exchangeRate.getTargetCurrencyId());
            preparedStatement.executeUpdate();
            return getByIds(exchangeRate.getBaseCurrencyId(), exchangeRate.getTargetCurrencyId()).get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

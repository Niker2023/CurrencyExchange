package org.petproject.dao;

import org.petproject.entity.ExchangeRate;
import org.petproject.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao  {

    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();


    private ExchangeRateDao() {}


    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }


    public Optional<ExchangeRate> getByIds(int baseCurrencyId, int targetCurrencyId) throws SQLException {

        String FIND_BY_CODES = """
                SELECT *
                FROM ExchangeRates
                WHERE BaseCurrencyId = ?
                AND TargetCurrencyId = ?
                """;

        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(FIND_BY_CODES)
                )
        {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            var id = resultSet.getInt("id");
            if (resultSet.wasNull()) {
              return Optional.empty();
            }
            return Optional.of(new ExchangeRate(id,
                    resultSet.getInt("BaseCurrencyId"),
                    resultSet.getInt("TargetCurrencyId"),
                    resultSet.getDouble("Rate")));
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    public Optional<ExchangeRate> save(ExchangeRate exchangeRate) throws SQLException {
        String INSERT_EXCHANGE_RATE = """
                INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
                VALUES (?, ?, ?)
                """;
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(INSERT_EXCHANGE_RATE)
                )
        {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setDouble(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();

            String FIND_LAST_INSERT = """
                    SELECT *
                    FROM ExchangeRates
                    ORDER BY id DESC
                    LIMIT 1
                    """;
            var resultSet = connection.prepareStatement(FIND_LAST_INSERT).executeQuery();
            resultSet.next();
            var id = resultSet.getInt("id");
            if (resultSet.wasNull()) {
                return Optional.empty();
            }
            return Optional.of(new ExchangeRate(id,
                    resultSet.getInt("BaseCurrencyId"),
                    resultSet.getInt("TargetCurrencyId"),
                    resultSet.getDouble("Rate")));
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    public Optional<ExchangeRate> update(ExchangeRate exchangeRate) throws SQLException {

        String UPDATE_RATE = """
                UPDATE ExchangeRates
                SET Rate = ?
                WHERE BaseCurrencyId = ?
                AND TargetCurrencyId = ?
                """;

        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(UPDATE_RATE)
                )
        {
            preparedStatement.setDouble(1, exchangeRate.getRate());
            preparedStatement.setInt(2, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(3, exchangeRate.getTargetCurrencyId());
            preparedStatement.executeUpdate();
            return getByIds(exchangeRate.getBaseCurrencyId(), exchangeRate.getTargetCurrencyId());
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    public List<ExchangeRate> getAll() throws SQLException {
        String FIND_ALL = """
                SELECT *
                FROM ExchangeRates
                """;
        try
                (
                        var connection = ConnectionManager.get();
                        var preparedStatement = connection.prepareStatement(FIND_ALL)
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
            throw new SQLException(e);
        }
    }
}

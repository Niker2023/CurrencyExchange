package org.petproject.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String PATH_TO_DB = "/home/nikita/IdeaProjects/CurrencyExchange/currency.db";
//    private static final String PATH_TO_DB = "/home/currency.db";  // for test 500 error
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;


    static {
        loadDriver();
    }


    private static void loadDriver() {
        System.setProperty("java.io.tmpdir", "/var/tmp");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private ConnectionManager() {
    }


    public static Connection get() throws SQLException {
        try {
            if (dataSource == null) {
                config.setJdbcUrl("jdbc:sqlite:" + PATH_TO_DB);
                dataSource = new HikariDataSource(config);
            }
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}
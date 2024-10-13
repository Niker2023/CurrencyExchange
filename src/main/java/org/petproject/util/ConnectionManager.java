package org.petproject.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String PATH_TO_DB = "/var/tmp/currency.db";
    private static HikariDataSource dataSource;


    static {
        setTmpDir();
    }


    private static void setTmpDir() {
        System.setProperty("java.io.tmpdir", "/var/tmp");
    }


    private static void createPoolConnections() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("SQLiteConnectionPool");
        config.setMaximumPoolSize(5);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + PATH_TO_DB);
        dataSource = new HikariDataSource(config);
    }


    private ConnectionManager() {}


    public static Connection get() throws SQLException {
            if (dataSource == null) {
                createPoolConnections();
            }
            return dataSource.getConnection();
    }
}
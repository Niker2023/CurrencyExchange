package org.petproject.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String PATH_TO_DB = "currency.db";

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionManager() {
    }

    public static Connection get() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

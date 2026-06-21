package com.msservices.geopolitik.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:db/middleeastdb.db";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Database not initialized. Call loadDataBases.load() first.");
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

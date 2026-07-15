package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String userName = "postgres";
    private static final String password = "12345";

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(JDBC_URL, userName, password);
        System.out.println("Connected to the database successfully!");
        return conn;
    }

}

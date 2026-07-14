package com.example;

import java.sql.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws SQLException {
        Connection conn = DriverManager.getConnection
                ("jdbc:postgresql://localhost:5432/postgres",
                        "postgres", "12345");

        String sql = "SELECT trackId, trackName FROM track";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString("trackName"));
        }

    }
}

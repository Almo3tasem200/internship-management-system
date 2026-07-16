package com.example;

import java.sql.*;
import java.util.*;

public class SubmissionDao {

    private final Connection conn;

    public SubmissionDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(int internId, int projectId, int score) {
        String sql = """
                insert into submission (internid, projectid, submitted_at, score)
                values (?, ?, CURRENT_TIMESTAMP(2), ?)
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internId);
            stmt.setInt(2, projectId);
            stmt.setInt(3, score);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted into submission.");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void displayAll() {
        String sql = """
                select * from submission
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                System.out.printf("Submission ID:%d, Intern ID: %d," +
                                " Project ID: %d, Submitted At: %s," +
                                " Score: %d%n",
                        res.getInt("submissionId"),
                        res.getInt("internId"),
                        res.getInt("projectId"),
                        res.getTimestamp("submitted_at"),
                        res.getInt("score"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void handleSQLException(SQLException e) {
        System.out.printf("""
                Database Error: %s
                SQL State: %s
                Error Code: %d
                """, e.getMessage(), e.getSQLState(), e.getErrorCode());
    }
}

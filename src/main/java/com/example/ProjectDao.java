package com.example;

import java.sql.*;

public class ProjectDao {

    private final Connection conn;

    public ProjectDao(Connection conn) {
        this.conn = conn;
    }

    public void assignToIntern(int internId, int projectId) {
        String sql = """
                insert into intern_project (internid, projectid)
                values (?, ?)
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internId);
            stmt.setInt(2, projectId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted into intern_project.");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void displayAllInternProjectAssignments() {
        String sql = """
                select i.internName, p.projectName
                from intern_project ip
                join intern i on ip.internid = i.internid
                join project p on ip.projectid = p.projectid
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                System.out.printf("Intern: %s, Project: %s%n",
                        res.getString("internName"),
                        res.getString("projectName"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void displayProjectsByInternName(String internName) {
        String sql = """
                select p.projectName
                from project p
                join intern_project ip on p.projectid = ip.projectid
                join intern i on i.internid = ip.internid
                where i.internname = ?;
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, internName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(" - " + rs.getString("projectName"));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void updateStatus(int projectId, String status) {
        String sql = """
                update project
                set status = ?
                where projectId = ?
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, projectId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated in project.");
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

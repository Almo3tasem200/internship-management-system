package com.example;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class InternDao {

    private final Connection conn;

    public InternDao(Connection conn) {
        this.conn = conn;
    }

    public List<Intern> findAll() {
        List<Intern> interns = new ArrayList<>();
        String sql = "SELECT * FROM intern";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Intern intern = new Intern(
                        rs.getInt("internId"),
                        rs.getString("internName"),
                        rs.getString("internEmail"),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getInt("trackId"),
                        rs.getInt("mentorId")
                );
                interns.add(intern);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return interns;
    }

    public Intern findById(int id) {
        String sql = "SELECT * FROM intern WHERE internId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Intern(
                            rs.getInt("internId"),
                            rs.getString("internName"),
                            rs.getString("internEmail"),
                            rs.getDate("startDate").toLocalDate(),
                            rs.getInt("trackId"),
                            rs.getInt("mentorId")
                    );
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    public void insert(String internName, String internEmail, LocalDate startDate, int trackId, Integer mentorId) {
        String sql = "INSERT INTO intern (internName, internEmail, startDate, trackId, mentorId) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, internName);
            stmt.setString(2, internEmail);
            stmt.setDate(3, Date.valueOf(startDate));
            stmt.setInt(4, trackId);
            if (mentorId != null)
                stmt.setInt(5, mentorId);
            else
                stmt.setNull(5, Types.INTEGER);

            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void updateMentor(int internId, Integer mentorId) {
        String sql = "UPDATE intern SET mentorId = ? WHERE internId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (mentorId == null)
                stmt.setNull(1, Types.INTEGER);
            else
                stmt.setInt(1, mentorId);

            stmt.setInt(2, internId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM intern WHERE internId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            handleSQLException(e);
            if (e.getSQLState().equals("23503")) {
                System.out.println("Cannot delete intern: intern is referenced in another table.");
            }
        }
    }

    public List<Intern> findByTrackName(String trackName) {
        List<Intern> interns = new ArrayList<>();
        String sql = """
                select i.internId, i.internName
                from intern i
                join track t on i.trackId = t.trackId
                where t.trackName = ?;
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trackName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Intern intern = new Intern(
                            rs.getInt("internId"),
                            rs.getString("internName")
                    );
                    interns.add(intern);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return interns;
    }

    public void displayWithTrackAndMentorNames() {
        String sql = """
                select i.internName,i.internEmail, t.trackName, m.mentorName 
                from intern i 
                join track t on i.trackId = t.trackId 
                left join mentor m on i.mentorId = m.mentorId;
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("""
                                -----------------
                                Intern Name:  %s
                                Intern Email:  %s
                                Track Name:  %s
                                Mentor Name:  %s
                                -----------------
                                """, rs.getString("internName"), rs.getString("internEmail"),
                        rs.getString("trackName"), rs.getString("mentorName"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void displayCountByTrack() {
        String sql = """
                select t.trackName,
                count(i.internId) as internCount
                from track t
                left join intern i on t.trackid = i.trackid
                group by t.trackName ;
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                System.out.printf("Track: %s, Number of Interns: %d%n",
                        res.getString("trackName"),
                        res.getInt("internCount"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void displayCountByMentor() {
        String sql = """
                select m.mentorName,
                count(i.internId) as internCount
                from mentor m
                left join intern i on m.mentorid = i.mentorid
                group by m.mentorName ;
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                System.out.printf("Mentor: %s, Number of Interns: %d%n",
                        res.getString("mentorName"),
                        res.getInt("internCount"));
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

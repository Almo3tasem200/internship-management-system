package com.example;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    Scanner scanner = new Scanner(System.in);
    String response = "";
    Connection conn;
    void main(String[] args) throws SQLException {
        // Exercise 1: Test the Database Connection
        System.out.println("1-) Test the Database Connection");
        DatabaseConfig db = new DatabaseConfig();
        conn = db.getConnection();

        if (conn != null) {
            // Exercise 2: Read All Interns
            System.out.println("2-) findAllInterns");
            List<Intern> interns = findAllInterns();
            for (Intern intern : interns) {
                System.out.printf("""
                                -------------------
                                Intern ID:  %s
                                Intern Name:  %s
                                Intern Email:  %s
                                Start Date:  %s
                                Track ID: %d
                                Mentor ID:  %d
                                -------------------
                                """, intern.getInternId(), intern.getInternName(),
                        intern.getInternEmail(), intern.getStartDate(),
                        intern.getTrackId(), intern.getMentorId());
            }
            System.out.println("*----------------------------*");

            // Exercise 3: Read Interns with Track and Mentor Names
            System.out.println("3-) DisplayInternWithTrackMentorName");
            DisplayInternWithTrackMentorName();
            System.out.println("*----------------------------*");

            // Exercise 4: Find Intern by ID
            System.out.print("""
                    4-) Find Intern by ID:
                    ______________________
                    """);
            while (true) {
                try {
                    System.out.print("Enter Intern ID: ");
                    int id = scanner.nextInt();
                    Intern i = findInternById(id);

                    if (i != null) {
                        System.out.printf("""
                                        -----------------
                                        Intern ID:  %s
                                        Intern Name:  %s
                                        Intern Email:  %s
                                        Start Date:  %s
                                        Track ID: %d
                                        Mentor ID:  %d
                                        -----------------
                                        """, i.getInternId(), i.getInternName(),
                                i.getInternEmail(), i.getStartDate(),
                                i.getTrackId(), i.getMentorId());
                        break;
                    } else {
                        System.out.printf("""
                                *Intern with ID %d not found*
                                - Try again
                                -----------------
                                """, id);
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid integer ID.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            scanner.nextLine(); // <-- consume the newline left by nextInt()
            System.out.println("*----------------------------*");

            // Exercise 5: Insert a New Intern
            System.out.print("""
                    5-) Insert a new Intern:-
                    -------------------------
                    Do you want to insert a new intern? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("*Inserting a new Intern:- *");
                System.out.println("Enter Intern Name: ");
                String internName = scanner.nextLine();
                System.out.println("Enter Intern Email: ");
                String internEmail = scanner.nextLine();
                System.out.println("Enter Start Date (YYYY-MM-DD): ");
                LocalDate startDate = LocalDate.parse(scanner.nextLine());
                System.out.println("Enter Track ID: ");
                int trackId = scanner.nextInt();
                scanner.nextLine(); // <-- consume the newline left by nextInt()
                System.out.println("Enter Mentor ID or (0 or leave a blank) for null: ");
                String inp = scanner.nextLine();
                Integer mentorId = (inp.isBlank() || "0".equals(inp)) ? null : Integer.parseInt(inp);
                insertIntern(internName, internEmail, startDate, trackId, mentorId);
            }
            System.out.println("*----------------------------*");

            // Exercise 6: Update an Intern’s Mentor
            System.out.print("""
                    6-) Update an Intern’s Mentor:-
                    -------------------------------
                    Do you want to update an intern's mentor? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("*Updating an Intern’s Mentor:- *");
                System.out.println("Enter Intern ID: ");
                int internId = scanner.nextInt();
                scanner.nextLine(); // <-- consume the newline left by nextInt()
                System.out.println("Enter New Mentor ID or (0 or leave a blank) for null: ");
                String input = scanner.nextLine();
                Integer mentorId = (input.isBlank() || "0".equals(input)) ? null : Integer.parseInt(input);
                updateInternMentor(internId, mentorId);
            }
            System.out.println("*----------------------------*");

            // Exercise 7: Delete an Intern
            System.out.print("""
                    7-) Delete an Intern:-
                    ----------------------
                    Do you want to delete an intern? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("*Deleting an Intern:- *");
                System.out.println("Enter Intern ID: ");
                int internId = scanner.nextInt();
                scanner.nextLine(); // <-- consume the newline left by nextInt()
                deleteInternById(internId);
            }
            System.out.println("*----------------------------*");

            // Exercise 8: Find Interns by Track Name
            System.out.println("8-) Find Interns by Track Name:-");
            System.out.println("Enter Track Name: ");
            String trackName = scanner.nextLine();
            List<Intern> internsByTrack = findInternsByTrackName(trackName);
            System.out.println("Interns in Track '" + trackName + "':");
            for (Intern intern : internsByTrack) {
                System.out.println(" - " + intern.getInternName());
            }
        }
    }

    List<Intern> findAllInterns() {

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
            e.printStackTrace();
        }
        return interns;
    }

    void DisplayInternWithTrackMentorName() {
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
            e.printStackTrace();
        }
    }

    Intern findInternById(int id) {
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
            e.printStackTrace();
        }
        return null;
    }

    void insertIntern(String internName, String internEmail, LocalDate startDate, int trackId, Integer mentorId) {

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
            e.printStackTrace();
        }
    }

    void updateInternMentor(int internId, Integer mentorId) {
        String sql = "UPDATE intern SET mentorId = ? WHERE internId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (mentorId == null)
                stmt.setNull(1, Types.INTEGER);
            else
                stmt.setInt(1, mentorId);

            stmt.setInt(2, internId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " row updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteInternById(int id) {
        String sql = "DELETE FROM intern WHERE internId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            if (e.getSQLState().equals("23503")) {
                //23503 is a PostgreSQL foreign key violation error code
                System.out.println("Cannot delete intern: intern is referenced in another table.");
            } else {
                e.printStackTrace();
            }
        }
        //What happens if the intern is referenced in another table?
        // It will return an error due to the foreign key constraint.
    }

    List<Intern> findInternsByTrackName(String trackName){
        List<Intern> interns = new ArrayList<>();
        String sql = """
                select i.internId, i.internName
                from intern i
                join track t on i.trackId = t.trackId
                where t.trackName = ?;
                """;
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trackName);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Intern intern = new Intern(
                            rs.getInt("internId"),
                            rs.getString("internName")
                    );
                    interns.add(intern);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interns;
    }
}


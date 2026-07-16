package com.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Main {

    private final Scanner scanner = new Scanner(System.in);

    void main(String[] args) throws SQLException {
        // Exercise 1: Test the Database Connection
        System.out.print("""
                1-) Test the Database Connection:
                --------------------------------
                """);
        DatabaseConfig db = new DatabaseConfig();
        Connection conn = db.getConnection();

        if (conn != null) {
            // Initialize DAOs
            InternDao internDao = new InternDao(conn);
            ProjectDao projectDao = new ProjectDao(conn);
            SubmissionDao submissionDao = new SubmissionDao(conn);

            // Exercise 2: Read All Interns
            System.out.print("""
                    2-) findAllInterns:
                    -------------------
                    Do you want to read All interns? (yes(y)/no(n)):
                    """);
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                List<Intern> interns = internDao.findAll();
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
            }
            System.out.println("*----------------------------*");

            // Exercise 3: Read Interns with Track and Mentor Names
            System.out.print("""
                    3-) DisplayInternWithTrackMentorName:
                    ------------------------------------
                    Do you want to read All interns? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                internDao.displayWithTrackAndMentorNames();
            }
            System.out.println("*----------------------------*");

            // Exercise 4: Find Intern by ID
            System.out.print("""
                    4-) Find Intern by ID:
                    ______________________
                    Do you want to find an intern by ID? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.print("Enter Intern ID: ");
                int id = scanner.nextInt();
                Intern i = internDao.findById(id);
                if (i == null) {
                    System.out.println("Intern with ID " + id + " not found.");
                } else {
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
                }

                scanner.nextLine();
            }
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
                scanner.nextLine();
                System.out.println("Enter Mentor ID or (0 or leave a blank) for null: ");
                String inp = scanner.nextLine();
                Integer mentorId = (inp.isBlank() || "0".equals(inp)) ? null : Integer.parseInt(inp);
                internDao.insert(internName, internEmail, startDate, trackId, mentorId);
            }
            System.out.println("*----------------------------*");

            // Exercise 6: Update an Intern's Mentor
            System.out.print("""
                    6-) Update an Intern's Mentor:-
                    -------------------------------
                    Do you want to update an intern's mentor? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("*Updating an Intern's Mentor:- *");
                System.out.println("Enter Intern ID: ");
                int internId = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter New Mentor ID or (0 or leave a blank) for null: ");
                String input = scanner.nextLine();
                Integer mentorId = (input.isBlank() || "0".equals(input)) ? null : Integer.parseInt(input);
                internDao.updateMentor(internId, mentorId);
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
                scanner.nextLine();
                internDao.deleteById(internId);
            }
            System.out.println("*----------------------------*");

            // Exercise 8: Find Interns by Track Name
            System.out.print("""
                    8-) Find Interns by Track Name:-
                    -------------------------------
                    Do you want to find interns by track name? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("Enter Track Name: ");
                String trackName = scanner.nextLine();
                List<Intern> internsByTrack = internDao.findByTrackName(trackName);
                System.out.println("Interns in Track '" + trackName + "':");
                for (Intern intern : internsByTrack) {
                    System.out.println(" - " + intern.getInternName());
                }
            }
            System.out.println("*----------------------------*");

            // Exercise 9: Show Projects Assigned to One Intern
            System.out.print("""
                    9-) Show Projects Assigned to One Intern:-
                    ------------------------------------------
                    Do you want to show projects for a specific intern? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("Enter Intern Name: ");
                String internName = scanner.nextLine();
                System.out.println("Projects assigned to " + internName + ":");
                projectDao.displayProjectsByInternName(internName);
            }
            System.out.println("*----------------------------*");

            // Exercise 10: Count Interns per Track
            System.out.print("""
                    10-) Count Interns per Track:-
                    ------------------------------
                    Do you want to Display the number of
                    interns in each track.? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                internDao.displayCountByTrack();
            }
            System.out.println("*----------------------------*");

            // Exercise 11: Count Interns per Mentor
            System.out.print("""
                    11-) Count Interns per Mentor:-
                    -------------------------------
                    Do you want Display the number of
                    interns assigned to each mentor? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                internDao.displayCountByMentor();
            }
            System.out.println("*----------------------------*");

            // Exercise 12: Assign a Project to an Intern
            System.out.print("""
                    12-) Assign a Project to an Intern:-
                    ------------------------------------
                    Do you want to assign a project to an intern? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("*Assigning a Project to an Intern:- *");
                System.out.println("Enter Intern ID: ");
                int internId = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter Project ID: ");
                int projectId = scanner.nextInt();
                scanner.nextLine();
                projectDao.assignToIntern(internId, projectId);
            }
            System.out.println("*----------------------------*");

            // Exercise 13: Show All Intern-Project Assignments
            System.out.print("""
                    13-) Show All Intern-Project Assignments:-
                    ------------------------------------
                    Do you want to show all intern-project assignments? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                projectDao.displayAllInternProjectAssignments();
            }
            System.out.println("*----------------------------*");

            // Exercise 14: Add and Read Submissions
            System.out.print("""
                    14-) Add and Read Submissions:-
                    ------------------------------------
                    Do you want to add and read submissions? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("*Adding a Submission:- *");

                System.out.println("Enter Intern ID: ");
                int internId = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter Project ID: ");
                int projectId = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter Score: ");
                int score = scanner.nextInt();
                scanner.nextLine();
                submissionDao.insert(internId, projectId, score);
                submissionDao.displayAll();
            }
            System.out.println("*----------------------------*");

            // Exercise 15: Update Project Status
            System.out.print("""
                    15-) Update Project Status:-
                    ----------------------------
                    Do you want to update a project's status? (yes(y)/no(n)):
                    """);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                System.out.println("Enter Project ID: ");
                int projectId = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter New Status: ");
                String status = scanner.nextLine();
                projectDao.updateStatus(projectId, status);
            }
            System.out.println("*----------------------------*");
        }
    }
}
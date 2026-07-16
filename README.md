*1) How to Run*

-Clone the repository:

            git clone <https://github.com/Almo3tasem200/internship-management-system.git>

-Open the project in IntelliJ IDEA:
            
            Select File → Open.
            Choose the cloned project folder.
            Wait for Maven to load all dependencies.
            
-Configure the PostgreSQL database:
            
            Make sure PostgreSQL is installed and running.
            Create the required database.
            Update the database connection settings (URL, username, and password) in the project configuration.

-Build the Maven project:
        
            mvn clean install
        
-Run the application:

            Open the main Java class in IntelliJ IDEA.
            Click Run or press Shift + F10.


          
            
*2) What Was Completed*

            -Connected Java application to PostgreSQL using JDBC.
            -Implemented reading and writing relational data.
            -Added SQL queries using joins and aggregation functions.
            -Used PreparedStatement for database operations.
            -Applied protection against SQL injection.
            -Structured JDBC code into clearer components.
            -Handled common database errors and constraint violations.
            -Completed the project as a Maven-based Java application.


            

*3) Q&A*

            1) Exercise 7: What happens if the intern is referenced in another table?
                    It will return an error due to the foreign key constraint.

            2) Exercise 15: What happens if you try an invalid value?
                    ERROR: new row for relation "project" violates
                        check constraint "project_status_check"
                    Detail: Failing row contains (2, UI Component Library, submitted).
            
            3) Exercise 16: Test at least 3 database error scenarios and observe what happens.
                            • duplicate intern email:
                                  Database Error: ERROR: duplicate key value violates
                                  unique constraint "intern_internemail_key"
                                           Detail: Key (internemail)=(laila@example.com) already exists.
                                  SQL State: 23505
                                  Error Code: 0
                            • invalid track_id:
                                  Database Error: ERROR: insert or update on table "intern" violates
                                  foreign key constraint "intern_trackid_fkey"
                                    Detail: Key (trackid)=(6) is not present in table "track".
                                  SQL State: 23503
                                  Error Code: 0
                            • duplicate entry in intern_projects
                                  Database Error: ERROR: insert or update on table "submission" violates
                                  foreign key constraint "submission_internid_projectid_fkey"
                                    Detail: Key (internid, projectid)=(1, 3) is not present in table "intern_project".
                                  SQL State: 23503
                                  Error Code: 0

                               
            
    

package db;

import java.sql.Connection;
import java.sql.Statement;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */

// Contains SQL code to create tables
public class CreateTables {
    public static void main(String[] args) {
        try (
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement()
        ) {
            // Create Staff table
            String createStaff = "CREATE TABLE Staff (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "name VARCHAR(100), " +
                "role VARCHAR(50), " + // admin, counselor, support
                "username VARCHAR(50), " +
                "password VARCHAR(100))";

            // Create Students table
            String createStudents = "CREATE TABLE Students (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "name VARCHAR(100), " +
                "email VARCHAR(100), " +
                "student_number VARCHAR(50))"; 

            // Create Counselors table
            String createCounselors = "CREATE TABLE Counselors (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "name VARCHAR(100), " +
                "specialization VARCHAR(100), " +
                //"availability VARCHAR(100), " +
                "active BOOLEAN)";

            // Create Programs table
            String createPrograms = "CREATE TABLE Programs (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "title VARCHAR(100), " +
                "description VARCHAR(255), " +
                "counselor_id INT, " +
                "FOREIGN KEY (counselor_id) REFERENCES Counselors(id))";

            // Create StudentPrograms table (many-to-many: students <-> programs)
            String createStudentPrograms = "CREATE TABLE StudentPrograms (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "student_id INT, " +
                "program_id INT, " +
                "enrolled_on DATE, " +
                "FOREIGN KEY (student_id) REFERENCES Students(id), " +
                "FOREIGN KEY (program_id) REFERENCES Programs(id))";

            // Create Appointments table
            String createAppointments = "CREATE TABLE Appointments (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "student_id INT, " +
                "counselor_id INT, " +
                "date DATE, " +
                "time TIME, " +
                "status VARCHAR(50), " +
                "FOREIGN KEY (student_id) REFERENCES Students(id), " +
                "FOREIGN KEY (counselor_id) REFERENCES Counselors(id))";

            // Create Feedback table
            String createFeedback = "CREATE TABLE Feedback (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "student_id INT, " +
                "rating INT, " +
                "comments VARCHAR(255), " +
                "FOREIGN KEY (student_id) REFERENCES Students(id))";

            // Execute all table creation statements in correct order
            stmt.executeUpdate(createStaff);
            stmt.executeUpdate(createStudents);
            stmt.executeUpdate(createCounselors);
            stmt.executeUpdate(createPrograms);
            stmt.executeUpdate(createStudentPrograms);
            stmt.executeUpdate(createAppointments);
            stmt.executeUpdate(createFeedback);

            System.out.println("✅ All tables created successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

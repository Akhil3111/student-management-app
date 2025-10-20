package com.myapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    // Database Connection String
    // We are using H2, a file-based database. 
    // It will create a 'studentdb.mv.db' file in our project folder.
    private static final String DB_URL = "jdbc:h2:./studentdb";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) {
        try {
            // Establish Connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("✅ Database connection established.");

            // Create our table (only if it doesn't exist)
            createStudentTable(conn);

            // Our console menu
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    addStudent(conn, name, email);
                } else if (choice.equals("2")) {
                    viewStudents(conn);
                } else if (choice.equals("3")) {
                    System.out.println("Exiting...");
                    break;
                }
            }
            conn.close(); // Close connection when done
            scanner.close();

        } catch (SQLException e) {
            System.err.println("❌ Database Error!");
            e.printStackTrace();
        }
    }

    // Syllabus: Statement
    public static void createStudentTable(Connection conn) throws SQLException {
        // A 'Statement' is for running simple, static SQL.
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Students (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "name VARCHAR(255), " +
                         "email VARCHAR(255) UNIQUE)";
            stmt.execute(sql);
            System.out.println("Table 'Students' is ready.");
        }
    }

    // Syllabus: PreparedStatement (THE BEST-PRACTICE)
    public static void addStudent(Connection conn, String name, String email) throws SQLException {
        // A 'PreparedStatement' is for SQL with parameters (the '?').
        // It's pre-compiled and SAFE from SQL Injection attacks.
        // ALWAYS use this for queries with user input.
        String sql = "INSERT INTO Students (name, email) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);  // Set the first '?'
            pstmt.setString(2, email); // Set the second '?'
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Success! " + rowsAffected + " student added.");
        }
    }

    // Syllabus: ResultSet and ResultSetMetaData
    public static void viewStudents(Connection conn) throws SQLException {
        System.out.println("\n--- Viewing All Students ---");
        String sql = "SELECT * FROM Students";

        try (Statement stmt = conn.createStatement()) {
            // A 'ResultSet' is the table of data returned by a SELECT query.
            ResultSet rs = stmt.executeQuery(sql);

            // 'ResultSetMetaData' is data ABOUT the data (column names, types)
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(meta.getColumnName(i).toUpperCase() + "\t\t");
            }
            System.out.println("\n---------------------------------");

            // We loop through the 'ResultSet' one row at a time with rs.next()
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println(id + "\t\t" + name + "\t\t" + email);
            }
        }
    }

    // Syllabus: CallableStatement (Just an explanation)
    // A 'CallableStatement' is used to execute "Stored Procedures"
    // (functions you save inside the database itself).
    // Example: String sql = "{call myProcedure(?, ?)}";
    // We don't need it for our simple app, but now you know what it's for!
}
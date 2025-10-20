package com.myapp;

// Import Java SQL (JDBC)
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement; // <-- MAKE SURE THIS IMPORT IS PRESENT

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addStudent")
public class StudentServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:h2:./studentdb";
    private static final String USER = "sa";
    private static final String PASS = "";

    // --- THIS IS THE CORRECT init() METHOD ---
    @Override
    public void init() throws ServletException {
        try {
            // 1. Load the H2 driver
            Class.forName("org.h2.Driver");
            
            // 2. Establish connection and create table
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement()) {
                
                String sql = "CREATE TABLE IF NOT EXISTS Students (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY, " +
                             "name VARCHAR(255), " +
                             "email VARCHAR(255) UNIQUE)";
                stmt.execute(sql);
                System.out.println("✅ Database table 'Students' is ready.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            // If the DB fails to start, this stops the app
            throw new ServletException("Database initialization failed", e);
        }
    }

    // --- YOUR doPost() METHOD IS PERFECT, KEEP IT AS-IS ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("studentName");
        String email = request.getParameter("studentEmail");
        String sql = "INSERT INTO Students (name, email) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new ServletException("Database error on insert", e);
        }

        // Redirect to the list servlet
        response.sendRedirect("listStudents");
    }
}
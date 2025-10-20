package com.myapp;

// Import Java SQL (JDBC)
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// This annotation links the URL "/addStudent" to this class
@WebServlet("/addStudent")
public class StudentServlet extends HttpServlet {
    
    // Our H2 Database constants from Unit 3
    private static final String DB_URL = "jdbc:h2:./studentdb";
    private static final String USER = "sa";
    private static final String PASS = "";

    // Syllabus: Servlet Life Cycle - init()
    // This runs ONCE when the servlet is first loaded by Tomcat.
    // It's the perfect place to create our table.
    @Override
    public void init() throws ServletException {
        try {
            // Load the H2 driver
            Class.forName("org.h2.Driver");
            
            // Get a connection
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement()) {
                
                // We use our exact SQL from Unit 3
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

    // Syllabus: Handling HTTP Request (POST)
    // This runs EVERY TIME a user submits the form.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Syllabus: Reading servlet parameters
        String name = request.getParameter("studentName");
        String email = request.getParameter("studentEmail");

        String sql = "INSERT INTO Students (name, email) VALUES (?, ?)";
        String message = "";

        // 2. Syllabus: Database Access using Servlets
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            
            message = "<h2>Success!</h2><p>Student " + name + " added.</p>";

        } catch (SQLException e) {
            message = "<h2>Error!</h2><p>Database error: " + e.getMessage() + "</p>";
            e.printStackTrace();
        }

        // 3. Syllabus: Handling HTTP Response
        // We send a simple HTML page back to the user
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Add Student</title><style>body{font-family: sans-serif; text-align: center; margin-top: 50px;}</style></head><body>");
        out.println(message);
        out.println("<a href='index.html'>Add Another Student</a>");
        out.println("</body></html>");
    }
}
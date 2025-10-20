package com.myapp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/listStudents")
public class ListStudentServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:h2:./studentdb";
    private static final String USER = "sa";
    private static final String PASS = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // This is our Model
        List<Student> studentList = new ArrayList<>();
        String sql = "SELECT * FROM Students";

        // This is our JDBC logic from Unit 3
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                
                // Add new Student bean to the list
                studentList.add(new Student(id, name, email));
            }

        } catch (SQLException e) {
            throw new ServletException("Database error while fetching students", e);
        }

        // --- This is the key MVC part ---
        
        // 1. Store the data (Model) in the request
        request.setAttribute("studentList", studentList);

        // 2. Get the RequestDispatcher for our View (JSP)
        RequestDispatcher dispatcher = request.getRequestDispatcher("student-list.jsp");

        // 3. Forward the request and response to the JSP
        dispatcher.forward(request, response);
    }
}
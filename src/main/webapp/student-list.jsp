<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.myapp.Student" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student List</title>
    <style>
        body { font-family: sans-serif; display: grid; place-items: center; background-color: #f9f9f9; }
        .container { background: #fff; border: 1px solid #ccc; padding: 25px; border-radius: 8px; margin-top: 30px; }
        table { border-collapse: collapse; width: 600px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <div class="container">
        <h2>All Registered Students</h2>
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
            </tr>
            
            <% 
                // This 'studentList' is set by our 'ListStudentServlet'
                List<Student> students = (List<Student>) request.getAttribute("studentList");
                
                // Loop through the list
                for (Student s : students) {
            %>
            
            <tr>
                <td><%= s.getId() %></td>
                <td><%= s.getName() %></td>
                <td><%= s.getEmail() %></td>
            </tr>

            <%
                } // End of the for loop
            %>

        </table>
        <br>
        <a href="index.html">Add Another Student</a>
    </div>
</body>
</html>
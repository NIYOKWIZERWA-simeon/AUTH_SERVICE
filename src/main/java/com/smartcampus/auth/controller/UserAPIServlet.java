package com.smartcampus.auth.controller;

import com.smartcampus.auth.model.User;
import com.smartcampus.auth.model.UserDAO;
// Correct Imports for Tomcat 10
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/users")
public class UserAPIServlet extends HttpServlet {

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    // READ (GET): List all users as JSON
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        List<User> users = userDAO.getAllUsers();

        // Manual JSON Construction (Because we can't use libraries)
        out.print("[");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            out.print("{");
            out.print("\"id\": " + u.getId() + ",");
            out.print("\"username\": \"" + u.getUsername() + "\",");
            out.print("\"email\": \"" + u.getEmail() + "\",");
            out.print("\"role\": \"" + u.getRole() + "\"");
            out.print("}");
            if (i < users.size() - 1) out.print(","); // Add comma except for last item
        }
        out.print("]");
        out.flush();
    }

    // DELETE: Remove a user via ID parameter (?id=5)
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("id");
        if(idParam == null) {
            response.setStatus(400);
            out.print("{\"message\": \"Missing id parameter\"}");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean deleted = userDAO.deleteUser(id);
            if(deleted) {
                out.print("{\"message\": \"User deleted successfully\"}");
            } else {
                response.setStatus(404);
                out.print("{\"message\": \"User not found\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(400);
            out.print("{\"message\": \"Invalid ID format\"}");
        }
    }
}
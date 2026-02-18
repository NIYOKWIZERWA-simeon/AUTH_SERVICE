package com.smartcampus.auth.controller;

import com.smartcampus.auth.model.User;
import com.smartcampus.auth.model.UserDAO;
// FIXED IMPORTS FOR TOMCAT 10
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/validate")
public class ValidationServlet extends HttpServlet {

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonBody = sb.toString();

        String token = "";
        if (jsonBody.contains("token")) {
            try {
                String[] parts = jsonBody.split("\"token\"");
                if (parts.length > 1) {
                    token = parts[1].replace(":", "").replace("\"", "").replace("}", "").trim();
                }
            } catch (Exception e) {
                // Ignore parsing errors
            }
        }

        if (token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\": \"error\", \"message\": \"Missing token\"}");
            return;
        }

        User user = userDAO.validateToken(token);

        if (user != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.print("{");
            out.print("\"status\": \"valid\",");
            out.print("\"userId\": " + user.getId() + ",");
            out.print("\"username\": \"" + user.getUsername() + "\",");
            out.print("\"role\": \"" + user.getRole() + "\"");
            out.print("}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"status\": \"invalid\", \"message\": \"Token not found or expired\"}");
        }
        out.flush();
    }
}
package com.smartcampus.auth.controller;

import com.smartcampus.auth.model.User;
import com.smartcampus.auth.service.AuthService; // Use Service, not DAO!
import java.io.IOException;

// CORRECT IMPORTS FOR TOMCAT 10
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService; // Changed from UserDAO

    public void init() {
        authService = new AuthService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 1. Use Service Layer logic
        User user = authService.loginUser(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // 2. Get Token via Service Layer
            String token = authService.getUserToken(user.getId());
            session.setAttribute("authToken", token);

            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("login.jsp?error=Invalid Credentials");
        }
    }
}
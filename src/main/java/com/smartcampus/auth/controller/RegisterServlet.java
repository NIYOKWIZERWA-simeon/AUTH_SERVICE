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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private AuthService authService; // Changed from UserDAO

    public void init() {
        authService = new AuthService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // 1. Use Service Layer (This handles validation logic too!)
        String result = authService.registerUser(username, email, password, role);

        if("SUCCESS".equals(result)) {
            response.sendRedirect("login.jsp?msg=Registered Successfully");
        } else {
            response.sendRedirect("register.jsp?error=" + result);
        }
    }
}
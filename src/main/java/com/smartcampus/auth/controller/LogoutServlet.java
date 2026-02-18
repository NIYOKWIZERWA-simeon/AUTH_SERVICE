package com.smartcampus.auth.controller;

// Correct Imports for Tomcat 10
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    // We use doGet because a simple link click (<a href...>) is a GET request
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get the current session (false means: don't create one if it doesn't exist)
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 2. Destroy the session
            session.invalidate();
        }

        // 3. Redirect back to login page
        response.sendRedirect("login.jsp?msg=Logged out successfully");
    }
}
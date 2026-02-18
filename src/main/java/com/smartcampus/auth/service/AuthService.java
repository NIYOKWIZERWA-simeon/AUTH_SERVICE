package com.smartcampus.auth.service;

import com.smartcampus.auth.model.User;
import com.smartcampus.auth.model.UserDAO;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {

    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    // Business Logic: Register
    public String registerUser(String username, String email, String password, String role) {
        // 1. Validation Logic
        if (username == null || username.trim().isEmpty()) return "Username cannot be empty";
        if (email == null || !email.contains("@")) return "Invalid email address";
        if (password == null || password.length() < 6) return "Password must be at least 6 characters";

        // 2. HASH THE PASSWORD (Security Step)
        String hashedPassword = hashPassword(password);

        // 3. Create User Object with HASHED password
        User user = new User(username, email, hashedPassword, role);

        // 4. Call DAO
        return userDAO.registerUser(user);
    }

    // Business Logic: Login
    public User loginUser(String email, String password) {
        if (email == null || password == null) return null;

        // 1. Hash the input password to match what is in the DB
        String hashedPassword = hashPassword(password);

        // 2. Check DB with hashed password
        return userDAO.loginUser(email, hashedPassword);
    }

    public String getUserToken(int userId) {
        return userDAO.getToken(userId);
    }

    // --- HELPER METHOD: SHA-256 Hashing ---
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert bytes to Hex String
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
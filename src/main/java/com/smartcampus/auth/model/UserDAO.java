package com.smartcampus.auth.model;

import com.smartcampus.auth.utils.DBConnection;
import java.sql.*;
import java.util.UUID;

public class UserDAO {

    // 1. REGISTER NEW USER
    public String registerUser(User user) {
        // We use try-with-resources to automatically close connections
        try (Connection conn = DBConnection.getConnection()) {

            // Check if email already exists
            String checkSql = "SELECT id FROM users WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, user.getEmail());
            if(checkStmt.executeQuery().next()) {
                return "Email already exists!";
            }

            // Insert User (Note: In real life, hash the password here!)
            String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword()); // Plain text for now

            int rows = pstmt.executeUpdate();

            if(rows > 0) {
                // Get the new User ID to assign a role
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) {
                    int userId = rs.getInt(1);
                    assignRole(conn, userId, user.getRole());
                    return "SUCCESS";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
        return "Registration Failed";
    }

    // Helper: Assign Role
    private void assignRole(Connection conn, int userId, String roleName) throws SQLException {
        // Find Role ID
        String findRole = "SELECT id FROM roles WHERE role_name = ?";
        PreparedStatement psRole = conn.prepareStatement(findRole);
        psRole.setString(1, roleName);
        ResultSet rs = psRole.executeQuery();

        if(rs.next()) {
            int roleId = rs.getInt("id");
            // Link User to Role
            String linkSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
            PreparedStatement psLink = conn.prepareStatement(linkSql);
            psLink.setInt(1, userId);
            psLink.setInt(2, roleId);
            psLink.executeUpdate();
        }
    }

    // 2. LOGIN USER
    public User loginUser(String email, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT u.id, u.username, u.email, r.role_name " +
                    "FROM users u " +
                    "JOIN user_roles ur ON u.id = ur.user_id " +
                    "JOIN roles r ON ur.role_id = r.id " +
                    "WHERE u.email = ? AND u.password_hash = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role_name"));

                // Generate and Save Token (Important for Microservices!)
                String token = saveToken(conn, user.getId(), user.getRole(), user.getEmail());
                // We can store the token in the User object temporarily if needed,
                // but usually, we put it in the Session in the Controller.
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login failed
    }

    // Helper: Create Token
    public String saveToken(Connection conn, int userId, String role, String email) throws SQLException {
        String token = UUID.randomUUID().toString();
        String sql = "INSERT INTO active_tokens (token_id, user_id, role_name, email) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, token);
        pstmt.setInt(2, userId);
        pstmt.setString(3, role);
        pstmt.setString(4, email);
        pstmt.executeUpdate();
        return token;
    }

    // Helper: Get Token for a User (To show on Dashboard)
    public String getToken(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT token_id FROM active_tokens WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) return rs.getString("token_id");
        } catch (SQLException e) { e.printStackTrace(); }
        return "No Token";
    }
    // 4. VALIDATE TOKEN (For Microservices)
    // Returns the User object if token is valid, null if invalid
    public User validateToken(String token) {
        User user = null;
        try (Connection conn = DBConnection.getConnection()) {
            // Join with users table to get full details if needed
            String sql = "SELECT u.id, u.username, u.email, t.role_name " +
                    "FROM active_tokens t " +
                    "JOIN users u ON t.user_id = u.id " +
                    "WHERE t.token_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, token);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role_name")); // Get role from token table
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    // 5. GET ALL USERS (For API)
    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT u.id, u.username, u.email, r.role_name FROM users u " +
                    "JOIN user_roles ur ON u.id = ur.user_id " +
                    "JOIN roles r ON ur.role_id = r.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role_name"));
                users.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return users;
    }

    // 6. DELETE USER (For API)
    public boolean deleteUser(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
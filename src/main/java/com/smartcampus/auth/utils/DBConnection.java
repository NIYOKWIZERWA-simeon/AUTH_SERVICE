package com.smartcampus.auth.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // XAMPP Default Credentials
    private static final String URL = "jdbc:mysql://localhost:3306/auth_service_db?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // XAMPP default is usually empty

    // Load the Driver once
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("ERROR: MySQL Driver not found!");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
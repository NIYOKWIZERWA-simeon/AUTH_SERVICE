-- 1. Create the Database
DROP DATABASE IF EXISTS auth_service_db;
CREATE DATABASE auth_service_db;
USE auth_service_db;

-- 2. Create 'roles' table (Required by assignment)
-- Stores the fixed roles: ADMIN, STUDENT, LECTURER
CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(20) NOT NULL UNIQUE
);

-- 3. Create 'users' table (Required by assignment)
-- Stores login credentials. Password must be hashed in the Java code.
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Create 'user_roles' table (Required by assignment)
-- Links users to roles (Many-to-Many relationship).
CREATE TABLE user_roles (
                            user_id INT,
                            role_id INT,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 5. Create 'active_tokens' table (Required for "Token validation logic")
-- This is how you allow other groups to verify a user without giving them DB access.
CREATE TABLE active_tokens (
                               token_id VARCHAR(64) PRIMARY KEY, -- This will be a UUID string
                               user_id INT NOT NULL,
                               email VARCHAR(100),               -- Stored for quick reference
                               role_name VARCHAR(20),            -- Stored to avoid complex joins later
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               expires_at TIMESTAMP NULL,        -- Optional: for token expiration
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
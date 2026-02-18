🎓 SmartCampus: Authentication Microservice
Group 1 | Distributed University Management System
📖 Project OverviewThe Authentication Service serves as the security backbone of the distributed SmartCampus ecosystem. As an independent microservice, it centralizes identity management while enabling seamless inter-service communication through secure token-based validation.

Core ResponsibilitiesIdentity Management: Secure registration and multi-role authentication (Admin, Student, Lecturer).
Cryptographic Security: Implementation of SHA-256 password hashing to ensure data integrity.
Session Orchestration: Advanced session handling using Jakarta EE HttpSession.
Distributed Validation: A specialized REST API enabling other SmartCampus modules (Student, Course, etc.) to verify user authenticity without direct database coupling.

🛠 Technology Stack
Layer,Technology
Language,Java 17+ (JDK)
Frameworks,"Jakarta EE Servlets, JSP (No Frameworks Constraint)"
Architecture,MVC (Model-View-Controller) with Service Layer
Database,MySQL (Isolated Schema)
Server,Apache Tomcat 10.1.x

⚙️ Engineering Setup1. 
Database Provisioning

Run the following script in your MySQL environment to initialize the auth_service_db.SQLCREATE DATABASE IF NOT EXISTS auth_service_db;
USE auth_service_db;

-- 1. Authorization Roles
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL UNIQUE
);

-- 2. Identity Store
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Role Mapping
CREATE TABLE user_roles (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 4. Microservice Token Store
CREATE TABLE active_tokens (
    token_id VARCHAR(64) PRIMARY KEY,
    user_id INT NOT NULL,
    role_name VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Seed Data
INSERT INTO roles (role_name) VALUES ('ADMIN'), ('STUDENT'), ('LECTURER');


2. Service ConfigurationUpdate the JDBC parameters in src/main/java/com/smartcampus/auth/util/DBConnection.java to match your local XAMPP/MySQL credentials.

🔌 API SpecificationToken Validation ServiceEndpoint: 
POST /api/validateUsed by secondary services (Student, Lecturer, etc.) to verify user state.Request 
Body (JSON):JSON{
  "token": "550e8400-e29b-41d4-a716-446655440000"
}
Response (200 OK):JSON{
  "status": "valid",
  "userId": 101,
  "username": "meekness",
  "role": "STUDENT"
}
Administrative EndpointsList Users: 
GET /api/users — Returns a JSON registry of all registered users.
Delete User: DELETE /api/users?id={id} — Permanently removes an identity from the system.

📁 Internal Project Structure
The project adheres to strict Separation of Concerns as mandated by the MVC architecture:
com.smartcampus.auth.model: POJOs representing the data state.
com.smartcampus.auth.dao: Data Access Objects handling JDBC logic.
com.smartcampus.auth.service: Business logic layer (Hashing, Validations).
com.smartcampus.auth.controller: Servlets managing the HTTP request/call lifecycle.
webapp: JSP-based View layer utilizing Bootstrap 5.
👥 Contributors

Meekness Bonheur (Reg No: 24rp03297) 

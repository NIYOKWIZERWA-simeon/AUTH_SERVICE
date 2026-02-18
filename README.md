
```markdown
# 🎓 SmartCampus: Authentication Microservice
### *Group 1 | Distributed University Management System*

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-10-003366?style=for-the-badge&logo=jakartaee&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Tomcat](https://img.shields.io/badge/Apache_Tomcat-10.1-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black)

</div>

---

## 📖 Project Overview
The **Authentication Service** serves as the central security gateway for the distributed **SmartCampus** ecosystem. Built as an independent microservice, it centralizes identity management while facilitating secure, cross-service communication via token-based validation.

### Key Features
* **Identity Management**: Secure registration and multi-role (Admin, Student, Lecturer) authentication flow.
* **Cryptographic Security**: Implementation of **SHA-256 password hashing** in the Service Layer.
* **Distributed Validation**: A specialized JSON API allowing secondary services to verify user state.
* **Session Orchestration**: Robust session lifecycle management using Jakarta EE `HttpSession`.

---

## 🛠 Technology Stack
| Layer | Technology |
| :--- | :--- |
| **Language** | Java 17+ (JDK) |
| **Frameworks** | Jakarta EE Servlets, JSP (Native Implementation) |
| **Architecture** | MVC (Model-View-Controller) with Service Layer |
| **Database** | MySQL (Isolated Schema) |
| **Server** | Apache Tomcat 10.1.x |

---

## ⚙️ Engineering Setup

### 1. Database Provisioning
Run the following script in your MySQL environment to initialize the **auth_service_db**.



```sql
CREATE DATABASE IF NOT EXISTS auth_service_db;
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

```

### 2. Service Configuration

Update the JDBC credentials in `src/main/java/com/smartcampus/auth/util/DBConnection.java` to match your local environment.

---

## 🔌 API Specification

### Token Validation Service

**Endpoint:** `POST /api/validate`

Used by Group 2 (Student Service) or Group 3 (Lecturer Service) to verify user state.

**Request Body (JSON):**

```json
{
  "token": "YOUR_UUID_TOKEN_HERE"
}

```

**Response (200 OK):**

```json
{
  "status": "valid",
  "userId": 101,
  "username": "meekness",
  "role": "STUDENT"
}

```

---

## 📁 Internal Project Structure

The project adheres to strict **Separation of Concerns**:

* `com.smartcampus.auth.model`: POJOs representing the data state.
* `com.smartcampus.auth.dao`: Data Access Objects handling JDBC logic.
* `com.smartcampus.auth.service`: Business logic layer (Hashing, Validations).
* `com.smartcampus.auth.controller`: Servlets managing the HTTP request/call lifecycle.
* `webapp`: JSP-based View layer utilizing Bootstrap 5.

---

## 👥 Contributors

* **Meekness Bonheur** (Reg No: 24rp03297) — *Lead Developer & Architect*

```


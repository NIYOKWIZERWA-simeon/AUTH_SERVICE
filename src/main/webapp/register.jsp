<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register - SmartCampus Auth</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #e9ecef; display: flex; align-items: center; justify-content: center; height: 100vh; }
        .card { width: 450px; }
    </style>
</head>
<body>

<div class="card p-4">
    <h3 class="text-center mb-4">Create Account</h3>

    <% String error = (String) request.getAttribute("errorMessage"); %>
    <% if(error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <form action="register" method="post">
        <div class="mb-3">
            <label class="form-label">Username</label>
            <input type="text" name="username" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Role</label>
            <select name="role" class="form-select">
                <option value="STUDENT">Student</option>
                <option value="LECTURER">Lecturer</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Password</label>
            <input type="password" name="password" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-success w-100">Register</button>
    </form>

    <div class="text-center mt-3">
        <a href="login.jsp">Already have an account? Login</a>
    </div>
</div>

</body>
</html>
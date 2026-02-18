<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.smartcampus.auth.model.User" %>

<%
  // --- 1. SESSION HANDLING & SECURITY ---

  // A. Prevent Browser Caching (Fixes the "Back Button" security hole)
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
  response.setHeader("Pragma", "no-cache"); // HTTP 1.0
  response.setDateHeader("Expires", 0); // Proxies

  // B. Check if User is Logged In
  User user = (User) session.getAttribute("user");
  String token = (String) session.getAttribute("authToken");

  // C. If not logged in, force redirect to Login
  if(user == null) {
    response.sendRedirect("login.jsp?error=Session Expired");
    return; // Stop loading the rest of the page
  }
%>

<!DOCTYPE html>
<html>
<head>
  <title>Dashboard - SmartCampus</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">Auth Service (Group 1)</a>
    <div class="d-flex">
        <span class="navbar-text text-white me-3">
            Signed in as: <strong><%= user.getUsername() %></strong>
        </span>
      <a href="logout" class="btn btn-outline-danger btn-sm">Logout</a>
    </div>
  </div>
</nav>

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-8">
      <div class="card shadow">
        <div class="card-header bg-primary text-white">
          <h5 class="mb-0">My Profile & Security</h5>
        </div>
        <div class="card-body">
          <p><strong>Role:</strong> <span class="badge bg-success"><%= user.getRole() %></span></p>
          <p><strong>Email:</strong> <%= user.getEmail() %></p>

          <hr>

          <div class="alert alert-warning">
            <h5>🔗 Inter-Service Token</h5>
            <p class="small text-muted">
              This token is your "Passport". Other groups (like Student Service or Course Service)
              will ask for this token to prove you are logged in.
            </p>
            <div class="input-group mb-3">
              <input type="text" class="form-control" value="<%= (token != null) ? token : "Generating..." %>" id="tokenBox" readonly>
              <button class="btn btn-outline-secondary" type="button" onclick="copyToken()">Copy Token</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  function copyToken() {
    var copyText = document.getElementById("tokenBox");
    copyText.select();
    copyText.setSelectionRange(0, 99999);
    navigator.clipboard.writeText(copyText.value);
    alert("Token copied! Ready to use in other microservices.");
  }
</script>

</body>
</html>
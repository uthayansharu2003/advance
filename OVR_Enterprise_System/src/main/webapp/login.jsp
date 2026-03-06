<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Ocean View Resort</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .alert-box {
            padding: 12px 15px; margin-bottom: 20px; border-radius: 6px;
            text-align: left; font-size: 0.95rem; display: flex; align-items: center; gap: 10px;
        }
        .alert-error {
            background-color: #fef2f2; color: #b91c1c;
            border: 1px solid #f87171; border-left: 5px solid #dc2626;
        }
        .alert-success {
            background-color: #f0fdf4; color: #15803d;
            border: 1px solid #86efac; border-left: 5px solid #16a34a;
        }
    </style>
</head>
<body class="login-body">

    <div class="login-card">
        <h2>🌊 Ocean View Resort</h2>
        <p style="color: #64748b; margin-bottom: 20px;">Secure Access Portal</p>

        <!-- The Error Box will now STAY on screen -->
        <% 
            String errorMsg = (String) request.getAttribute("errorMsg");
            if (errorMsg != null && !errorMsg.isEmpty()) { 
        %>
            <div class="alert-box alert-error">
                <span style="font-size: 1.2rem;">⚠️</span>
                <span><strong>Access Denied:</strong> <%= errorMsg %></span>
            </div>
        <% } %>

        <% 
            String logoutMsg = request.getParameter("logout");
            if ("success".equals(logoutMsg)) { 
        %>
            <div class="alert-box alert-success">
                <span style="font-size: 1.2rem;">✅</span>
                <span>Successfully logged out safely.</span>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="POST">
            <div class="form-group">
                <label for="username">Username</label>
                <!-- Retains the entered username if login fails -->
                <input type="text" id="username" name="username" required 
                       value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"
                       placeholder="Enter your username">
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required 
                       minlength="6" placeholder="Enter your password">
            </div>
            
            <button type="submit" class="btn">Secure Login</button>
        </form>
    </div>

</body>
</html>
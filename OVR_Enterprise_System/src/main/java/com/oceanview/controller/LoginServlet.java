package com.oceanview.controller;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    // Displays the login page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // Processes the login form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username").trim(); // Trim spaces!
        String plainPassword = request.getParameter("password");

        // Hash the input password using our Phase 1 Utility
        String passwordHash = SecurityUtil.hashPassword(plainPassword);

        // --- DEBUG PRINTS (Check your Eclipse Console tab!) ---
        System.out.println("====== LOGIN ATTEMPT ======");
        System.out.println("Username entered: [" + username + "]");
        System.out.println("Raw Password entered: [" + plainPassword + "]");
        System.out.println("Generated Hash: [" + passwordHash + "]");
        System.out.println("===========================");

        // Verify with database
        User user = userDAO.authenticateUser(username, passwordHash);

        if (user != null) {
            System.out.println("=> SUCCESS: User matched in DB. Role: " + user.getRole());
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedUser", user);
            session.setAttribute("flashSuccess", "Welcome back, " + user.getUsername() + "!");

            if ("ADMIN".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
            }
        } else {
            System.out.println("=> FAILED: No match in DB for that username + hash combination.");
            request.setAttribute("errorMsg", "Invalid username or password. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
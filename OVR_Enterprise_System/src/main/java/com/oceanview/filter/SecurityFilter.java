package com.oceanview.filter;

import com.oceanview.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Intercepts all requests to ensure the user is authenticated and authorized.
 * Implements strict cache control to prevent the "Back Button" vulnerability.
 */
@WebFilter("/*") // Intercepts ALL URLs
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String loginURI = request.getContextPath() + "/login";
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // 1. CRITICAL: Prevent Browser Caching of protected pages
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        // 2. Define Public Resources (Paths that don't need login)
        boolean isLoginRequest = path.equals("/login");
        boolean isApiRequest = path.startsWith("/api/"); // Allow AJAX
        boolean isCssOrAssets = path.startsWith("/css/") || path.startsWith("/assets/");
        boolean isTestRequest = path.equals("/test.jsp"); // ADD THIS LINE
        
        boolean isLoggedIn = (session != null && session.getAttribute("loggedUser") != null);

        if (isLoggedIn && isLoginRequest) {
            // If already logged in and tries to go to login page, redirect to their dashboard
            User user = (User) session.getAttribute("loggedUser");
            if ("ADMIN".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
            }
        } else if (isLoggedIn || isLoginRequest || isCssOrAssets || isApiRequest || isTestRequest) { // ADD isTestRequest HERE
            
            // 3. Role-Based Access Control (RBAC) Logic
            if (isLoggedIn && !isLoginRequest && !isApiRequest) {
                User user = (User) session.getAttribute("loggedUser");
                
                // Block Staff from accessing Admin pages
                if (path.startsWith("/admin/") && !"ADMIN".equals(user.getRole())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin Privileges Required.");
                    return;
                }
            }
            
            // Allow the request to proceed
            chain.doFilter(request, response);
            
        } else {
            // Not logged in and requesting a protected page -> Redirect to login
            response.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {}
}
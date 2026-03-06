<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Business Reports - Admin Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .kpi-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 30px; }
        .kpi-card { background: white; padding: 25px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.05); border-left: 5px solid var(--primary-color); }
        .kpi-card h4 { color: #64748b; margin-bottom: 10px; font-size: 1rem; text-transform: uppercase; letter-spacing: 1px; }
        .kpi-card .value { font-size: 2rem; font-weight: bold; color: var(--text-dark); }
        .kpi-card.revenue { border-left-color: #10b981; } /* Green */
        .kpi-card.revenue .value { color: #10b981; }
    </style>
</head>
<body>

<div class="dashboard-container">
    <aside class="sidebar" style="background: #0f172a;">
        <div class="sidebar-header" style="color: #38bdf8;">👑 Admin Portal</div>
        <ul class="sidebar-menu">
            <li><a href="${pageContext.request.contextPath}/admin/dashboard">📅 Reservations</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reports" class="active">📊 Business Reports</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/help.jsp">📖 Admin Manual</a></li>
        </ul>
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/logout" class="btn" style="background: #ef4444; text-align: center;">Logout</a>
        </div>
    </aside>

    <main class="main-content">
        <header class="top-header">
            <h2>Financial & Booking Analytics</h2>
            <div class="user-info">Logged in as: <strong>${sessionScope.loggedUser.username}</strong></div>
        </header>

        <div class="content-wrapper" style="display: block;">
            
            <!-- KPI CARDS -->
            <div class="kpi-grid">
                <div class="kpi-card revenue">
                    <h4>Total Gross Revenue</h4>
                    <div class="value">
                        <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="LKR " maxFractionDigits="2"/>
                    </div>
                </div>
                <div class="kpi-card">
                    <h4>Total Lifetime Bookings</h4>
                    <div class="value">${totalBookings}</div>
                </div>
            </div>

            <!-- PERFORMANCE TABLE -->
            <section class="card" style="width: 100%;">
                <h3>🏨 Revenue Breakdown by Room Type</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Room Category</th>
                            <th>Total Bookings</th>
                            <th style="text-align: right;">Generated Revenue</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="stat" items="${roomTypeStats}">
                            <tr>
                                <td><strong>${stat.categoryName}</strong></td>
                                <td>${stat.totalBookings} Reservations</td>
                                <td style="text-align: right; font-weight: bold; color: #0f766e;">
                                    <fmt:formatNumber value="${stat.totalRevenue}" type="currency" currencySymbol="LKR " maxFractionDigits="2"/>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty roomTypeStats}">
                            <tr><td colspan="3" style="text-align:center;">No revenue data available yet.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </section>
        </div>
    </main>
</div>
</body>
</html>
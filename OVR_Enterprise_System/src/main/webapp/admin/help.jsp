<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Manual - Ocean View</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="dashboard-container">
    <aside class="sidebar" style="background: #0f172a;">
        <div class="sidebar-header" style="color: #38bdf8;">👑 Admin Portal</div>
        <ul class="sidebar-menu">
            <li><a href="${pageContext.request.contextPath}/admin/dashboard">📅 Reservations</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reports">📊 Business Reports</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/help.jsp" class="active">📖 Admin Manual</a></li>
        </ul>
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/logout" class="btn" style="background: #ef4444; text-align: center;">Logout</a>
        </div>
    </aside>

    <main class="main-content">
        <header class="top-header">
            <h2>Admin Management Guide</h2>
        </header>

        <div class="content-wrapper" style="display: block;">
            <section class="card" style="max-width: 800px; margin: 0 auto;">
                <h3 style="color: #38bdf8;">Executive Control Center Guidelines</h3>
                
                <h4 style="color: var(--primary-color); margin-top: 25px;">1. System Overview</h4>
                <p style="color: var(--text-dark); line-height: 1.6;">As an Administrator, you have full override capabilities for front-desk operations, alongside exclusive access to financial reporting. Staff members are blocked from viewing the Reports page via the SecurityFilter.</p>

                <h4 style="color: var(--primary-color); margin-top: 25px;">2. Business Reports (Analytics)</h4>
                <ul style="margin-left: 20px; line-height: 1.6; color: var(--text-dark);">
                    <li><strong>Gross Revenue:</strong> Displays the sum of all confirmed check-outs and active bookings in Sri Lankan Rupees (LKR).</li>
                    <li><strong>Category Breakdown:</strong> Identifies which room types yield the highest profit margins to assist in future marketing strategies.</li>
                </ul>

                <hr style="margin: 30px 0; border: 0; border-top: 1px dashed #cbd5e1;">
                
                <div class="alert" style="background: #fef2f2; border-left: 5px solid #dc2626; padding: 15px;">
                    <strong>Security Notice:</strong> Always log out of the Admin Portal before leaving your terminal. The system prevents "Back Button" exploits automatically, but physical terminal security is your responsibility.
                </div>
            </section>
        </div>
    </main>
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Staff User Manual - Ocean View</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="dashboard-container">
    <!-- SIDEBAR -->
    <aside class="sidebar">
        <div class="sidebar-header">🌊 Ocean View</div>
        <ul class="sidebar-menu">
            <li><a href="${pageContext.request.contextPath}/staff/dashboard">📅 Reservations</a></li>
            <li><a href="${pageContext.request.contextPath}/staff/help.jsp" class="active">📖 Help/Manual</a></li>
        </ul>
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/logout" class="btn" style="background: #ef4444; text-align: center;">Logout</a>
        </div>
    </aside>

    <!-- MAIN CONTENT -->
    <main class="main-content">
        <header class="top-header">
            <h2>User Manual & Guidelines</h2>
            <div class="user-info">Logged in as: <strong>${sessionScope.loggedUser.username}</strong></div>
        </header>

        <div class="content-wrapper" style="display: block;"> <!-- Override grid to block for document view -->
            <section class="card" style="max-width: 800px; margin: 0 auto;">
                <h3>Ocean View Resort - Front Desk System Guide</h3>
                <p style="margin-bottom: 20px; color: #64748b;">Welcome to the new digital reservation system. Follow these guidelines to ensure accurate guest management.</p>

                <h4 style="color: var(--primary-color); margin-top: 25px;">1. Making a New Reservation</h4>
                <ol style="margin-left: 20px; margin-top: 10px; line-height: 1.6; color: var(--text-dark);">
                    <li>Navigate to the <strong>Reservations</strong> tab on the sidebar.</li>
                    <li>Fill in the Guest's Name, Contact Number, and Address in the left-hand form.</li>
                    <li>Select the requested <strong>Room Type</strong> from the dropdown menu.</li>
                    <li>Choose the <strong>Check-in</strong> and <strong>Check-out</strong> dates. 
                        <br><small style="color: var(--accent-color);">*Note: The system will automatically check database availability and calculate the price including the 10% tax/service charge.</small>
                    </li>
                    <li>If a room is available, it will populate in the <em>"Select Available Room"</em> dropdown. Select one.</li>
                    <li>Click <strong>Confirm Booking</strong>. A unique Reservation Number (e.g., OVR-2025-XXXX) will be generated and displayed at the top of your screen.</li>
                </ol>

                <h4 style="color: var(--primary-color); margin-top: 25px;">2. Viewing Existing Reservations</h4>
                <ul style="margin-left: 20px; margin-top: 10px; line-height: 1.6; color: var(--text-dark);">
                    <li>All confirmed bookings appear in the <strong>Recent Reservations</strong> table on the right side of the dashboard.</li>
                    <li>The system prevents double-booking automatically. If a guest calls to check dates, use the new reservation form to test availability without clicking submit.</li>
                </ul>

                <h4 style="color: var(--primary-color); margin-top: 25px;">3. Generating a Bill/Invoice</h4>
                <ul style="margin-left: 20px; margin-top: 10px; line-height: 1.6; color: var(--text-dark);">
                    <li>In the reservations table, click the <strong>"Print Bill"</strong> link next to a specific booking.</li>
                    <li>This will open a printable digital invoice that you can print (Ctrl+P) and hand to the guest upon check-out.</li>
                </ul>

                <hr style="margin: 30px 0; border: 0; border-top: 1px dashed #cbd5e1;">
                
                <div class="alert info" style="background: #e0f2fe; border-left: 5px solid #0ea5e9; padding: 15px; border-radius: 4px;">
                    <strong>Technical Support:</strong> If the system displays an error or fails to calculate prices, please check your network connection or contact the Admin/IT Department.
                </div>
            </section>
        </div>
    </main>
</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Staff Dashboard - Ocean View</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="dashboard-container">
    <!-- SIDEBAR -->
    <aside class="sidebar">
        <div class="sidebar-header">🌊 Ocean View</div>
        <ul class="sidebar-menu">
            <li><a href="${pageContext.request.contextPath}/staff/dashboard" class="active">📅 Reservations</a></li>
            <li><a href="${pageContext.request.contextPath}/staff/help.jsp">📖 Help/Manual</a></li>
        </ul>
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/logout" class="btn" style="background: #ef4444; text-align: center;">Logout</a>
        </div>
    </aside>

    <!-- MAIN CONTENT -->
    <main class="main-content">
        <header class="top-header">
            <h2>Front Desk Operations</h2>
            <div class="user-info">Logged in as: <strong>${sessionScope.loggedUser.username}</strong></div>
        </header>

        <!-- Flash Messages -->
        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="alert success" style="margin: 20px 30px 0 30px;">✅ ${sessionScope.flashSuccess}</div>
            <c:remove var="flashSuccess" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.flashError}">
            <div class="alert error" style="margin: 20px 30px 0 30px;">⚠️ ${sessionScope.flashError}</div>
            <c:remove var="flashError" scope="session" />
        </c:if>

        <div class="content-wrapper">
            <!-- LEFT COLUMN: NEW BOOKING FORM -->
            <section class="card">
                <h3>➕ New Reservation</h3>
                <form action="${pageContext.request.contextPath}/reservation/add" method="POST">
                    
                    <div class="form-group">
                        <label>Guest Name *</label>
                        <input type="text" name="guestName" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Contact Number (Sri Lanka) *</label>
                        <!-- VALIDATION: Must start with 0, contain 10 digits total -->
                        <input type="tel" name="guestContact" required 
                               pattern="0[0-9]{9}" 
                               maxlength="10" 
                               title="Please enter a valid 10-digit SL number (e.g., 0771234567)"
                               placeholder="07xxxxxxxx">
                    </div>
                    
                    <div class="form-group">
                        <label>Home Address *</label>
                        <input type="text" name="guestAddress" required>
                    </div>

                    <hr style="margin: 20px 0; border: 0; border-top: 1px solid #e2e8f0;">

                    <div class="form-group">
                        <label>Room Type *</label>
                        <select id="typeId" name="typeId" required onchange="checkAvailability()">
                            <option value="">-- Select Type --</option>
                            <c:forEach var="type" items="${roomTypes}">
                                <!-- Format currency to LKR -->
                                <option value="${type.typeId}">${type.typeName} (LKR ${type.baseRate}/night)</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div style="display: flex; gap: 15px;">
                        <div class="form-group" style="flex: 1;">
                            <label>Check-in Date *</label>
                            <input type="date" id="checkIn" name="checkIn" required onchange="checkAvailability()">
                        </div>
                        <div class="form-group" style="flex: 1;">
                            <label>Check-out Date *</label>
                            <input type="date" id="checkOut" name="checkOut" required onchange="checkAvailability()">
                        </div>
                    </div>

                    <!-- AJAX DYNAMIC UI ELEMENTS -->
                    <div id="priceDisplay" class="price-display" style="display: none;">
                        Nights: <span id="nightCount">0</span> | Total: LKR <span id="totalCost">0.00</span>
                    </div>

                    <div class="form-group">
                        <label>Select Available Room *</label>
                        <select id="roomId" name="roomId" required disabled>
                            <option value="">Please select dates and type first...</option>
                        </select>
                    </div>

                    <button type="submit" class="btn" id="submitBtn" disabled>Confirm Booking</button>
                </form>
            </section>

            <!-- RIGHT COLUMN: RESERVATIONS TABLE -->
            <section class="card" style="grid-column: 1 / span 2; width: 100%;"> 
                <!-- Note: I changed the grid layout to span full width for the table -->
                <h3>📋 Recent Reservations (Detailed View)</h3>
                <div style="overflow-x: auto;"> <!-- Enables horizontal scroll for many columns -->
                    <table style="font-size: 0.9rem;">
                        <thead>
                            <tr>
                                <th>Res No.</th>
                                <th>Booked At</th>
                                <th>Guest Details</th>
                                <th>Room</th>
                                <th>Check In</th>
                                <th>Check Out</th>
                                <th>Total Cost</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="res" items="${reservations}">
                                <tr>
                                    <td><strong style="color: var(--primary-color);">${res.reservationNo}</strong></td>
                                    <td style="font-size: 0.8rem; color: #64748b;">${res.createdAt}</td>
                                    <td>
                                        <strong>${res.guestName}</strong><br>
                                        <small>${res.guestContact} | ${res.guestAddress}</small>
                                    </td>
                                    <td><span class="badge" style="background:#f1f5f9; color:var(--text-dark);">Room ${res.roomNumber}</span></td>
                                    <td>${res.checkIn}</td>
                                    <td>${res.checkOut}</td>
                                    <td><strong style="color:var(--success);">LKR ${res.totalCost}</strong></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${res.status == 'CONFIRMED'}">
                                                <span class="badge confirmed" style="background:#dcfce7; color:#166534;">${res.status}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge" style="background:#e2e8f0; color:#475569;">${res.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/invoice?id=${res.reservationNo}" 
                                           class="btn" style="padding: 5px 10px; font-size: 0.8rem; width: auto; display: inline-block;">
                                           Print Bill
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty reservations}">
                                <tr><td colspan="9" style="text-align:center; padding: 40px;">No records found in the system.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    </main>
</div>

<!-- ================= AJAX LOGIC ================= -->
<script>
    // Set minimum date for Check-in to TODAY
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('checkIn').setAttribute('min', today);

    function checkAvailability() {
        const typeId = document.getElementById('typeId').value;
        const checkIn = document.getElementById('checkIn').value;
        const checkOut = document.getElementById('checkOut').value;
        
        const roomSelect = document.getElementById('roomId');
        const submitBtn = document.getElementById('submitBtn');
        const priceDisplay = document.getElementById('priceDisplay');

        // Automatically set Checkout min date based on Checkin
        if(checkIn) {
            document.getElementById('checkOut').setAttribute('min', checkIn);
        }

        // Only trigger API if all 3 fields are filled
        if (typeId && checkIn && checkOut) {
            
            if(new Date(checkOut) <= new Date(checkIn)) {
                // Don't alert yet, just wait for user to fix date
                return;
            }

            roomSelect.innerHTML = '<option value="">Loading available rooms...</option>';
            roomSelect.disabled = true;

            // Construct API URL
            const apiUrl = "${pageContext.request.contextPath}/api/availability?typeId=" + typeId + "&checkIn=" + checkIn + "&checkOut=" + checkOut;
            console.log("Fetching: " + apiUrl); // DEBUG LOG FOR BROWSER CONSOLE

            fetch(apiUrl)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.error) {
                        console.error("API Error: " + data.error);
                        roomSelect.innerHTML = '<option value="">Error: ' + data.error + '</option>';
                        return;
                    }

                    // Update dynamic pricing UI
                    document.getElementById('nightCount').innerText = data.nights;
                    // Format money with commas (e.g. 45,000.00)
                    document.getElementById('totalCost').innerText = data.totalCost.toLocaleString('en-LK', {minimumFractionDigits: 2});
                    priceDisplay.style.display = 'block';

                    // Populate dynamic Room Dropdown
                    roomSelect.innerHTML = '<option value="">-- Select a Room --</option>'; 
                    
                    if (data.rooms && data.rooms.length > 0) {
                        let hasAvailable = false;
                        data.rooms.forEach(room => {
                            let option = document.createElement('option');
                            option.value = room.id;
                            
                            // Format the text and disable if not available
                            if (room.status === 'AVAILABLE') {
                                option.text = "Room " + room.number + " - ✅ Available";
                                option.disabled = false;
                                hasAvailable = true;
                            } else if (room.status === 'BOOKED') {
                                option.text = "Room " + room.number + " - ❌ Booked";
                                option.disabled = true;
                                option.style.color = "grey";
                            } else if (room.status === 'MAINTENANCE') {
                                option.text = "Room " + room.number + " - 🛠️ Maintenance";
                                option.disabled = true;
                                option.style.color = "grey";
                            }
                            
                            roomSelect.appendChild(option);
                        });
                        roomSelect.disabled = false;
                        submitBtn.disabled = !hasAvailable;
                    } else {
                        roomSelect.innerHTML = '<option value="">No rooms found for this category.</option>';
                        submitBtn.disabled = true;
                    }
                })
                .catch(error => {
                    console.error('Fetch Error:', error);
                    roomSelect.innerHTML = '<option value="">⚠️ System Error (See Console)</option>';
                });
        }
    }
</script>
</body>
</html>
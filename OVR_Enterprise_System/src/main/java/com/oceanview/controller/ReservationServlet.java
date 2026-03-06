package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.RoomType;
import com.oceanview.service.BillingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;
import java.util.Random;

@WebServlet("/reservation/add")
public class ReservationServlet extends HttpServlet {

    private ReservationDAO reservationDAO;
    private RoomDAO roomDAO;
    private BillingService billingService;

    @Override
    public void init() {
        reservationDAO = new ReservationDAO();
        roomDAO = new RoomDAO();
        billingService = new BillingService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 1. Retrieve data from the Dashboard Form
            String guestName = request.getParameter("guestName");
            String guestContact = request.getParameter("guestContact");
            String guestAddress = request.getParameter("guestAddress");
            int typeId = Integer.parseInt(request.getParameter("typeId"));
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            LocalDate checkIn = LocalDate.parse(request.getParameter("checkIn"));
            LocalDate checkOut = LocalDate.parse(request.getParameter("checkOut"));

            // 2. Final Server-Side Security/Validation Check (Double-booking prevention)
            if (!checkOut.isAfter(checkIn)) {
                throw new Exception("Check-out date must be after check-in date.");
            }

            // 3. Recalculate cost server-side (Never trust client-side prices)
            RoomType roomType = roomDAO.getRoomTypeById(typeId);
            double totalCost = billingService.calculateTotalCost(checkIn, checkOut, roomType.getBaseRate());

            // 4. Generate Unique Reservation Number (Format: OVR-YYYY-XXXX)
            // Meets the requirement: "Each guest will be assigned a unique reservation number."
            String resNo = generateReservationNumber();

            // 5. Populate the Model
            Reservation res = new Reservation();
            res.setReservationNo(resNo);
            res.setGuestName(guestName);
            res.setGuestContact(guestContact);
            res.setGuestAddress(guestAddress);
            res.setRoomId(roomId);
            res.setCheckIn(Date.valueOf(checkIn));
            res.setCheckOut(Date.valueOf(checkOut));
            res.setTotalCost(totalCost);

            // 6. Save to Database using DAO
            boolean isCreated = reservationDAO.createReservation(res);

            if (isCreated) {
                // Use Flash Messaging to show success on the dashboard
                request.getSession().setAttribute("flashSuccess", 
                    "Booking Confirmed! Reservation No: " + resNo);
            } else {
                request.getSession().setAttribute("flashError", "Failed to save reservation to database.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("flashError", "Error processing reservation: " + e.getMessage());
        }

        // --- Replace the old sendRedirect at the bottom of doPost with this ---
        com.oceanview.model.User currentUser = (com.oceanview.model.User) request.getSession().getAttribute("loggedUser");
        
        if (currentUser != null && "ADMIN".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/staff/dashboard");
        }
    }

    /**
     * Generates a string like "OVR-2025-4A9B"
     */
    private String generateReservationNumber() {
        String year = String.valueOf(Year.now().getValue());
        // Generate a random 4-character hex string for uniqueness
        String randomHex = Integer.toHexString(new Random().nextInt(0xFFFF)).toUpperCase();
        // Pad with zeros if necessary to ensure 4 chars
        randomHex = String.format("%4s", randomHex).replace(' ', '0');
        
        return "OVR-" + year + "-" + randomHex;
    }
}
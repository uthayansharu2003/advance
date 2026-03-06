package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.RoomType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/staff/dashboard")
public class StaffDashboardServlet extends HttpServlet {

    private RoomDAO roomDAO;
    private ReservationDAO reservationDAO;

    @Override
    public void init() {
        roomDAO = new RoomDAO();
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Fetch data required for the UI
        List<RoomType> roomTypes = roomDAO.getAllRoomTypes();
        List<Reservation> recentReservations = reservationDAO.getAllReservations();

        // Attach data to request
        request.setAttribute("roomTypes", roomTypes);
        request.setAttribute("reservations", recentReservations);

        // Forward to the JSP
        request.getRequestDispatcher("/staff/dashboard.jsp").forward(request, response);
    }
}
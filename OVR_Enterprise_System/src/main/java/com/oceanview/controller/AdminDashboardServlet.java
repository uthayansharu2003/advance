package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private RoomDAO roomDAO;
    private ReservationDAO reservationDAO;

    @Override public void init() {
        roomDAO = new RoomDAO();
        reservationDAO = new ReservationDAO();
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("roomTypes", roomDAO.getAllRoomTypes());
        request.setAttribute("reservations", reservationDAO.getAllReservations());
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
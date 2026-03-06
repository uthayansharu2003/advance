package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/reports")
public class AdminReportServlet extends HttpServlet {
    private ReservationDAO reservationDAO;

    @Override public void init() { reservationDAO = new ReservationDAO(); }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalRevenue", reservationDAO.getTotalRevenue());
        request.setAttribute("totalBookings", reservationDAO.getTotalBookingsCount());
        request.setAttribute("roomTypeStats", reservationDAO.getRevenueByRoomType());
        
        request.getRequestDispatcher("/admin/reports.jsp").forward(request, response);
    }
}
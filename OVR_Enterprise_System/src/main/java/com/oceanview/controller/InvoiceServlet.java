package com.oceanview.controller;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/invoice")
public class InvoiceServlet extends HttpServlet {

    private ReservationDAO reservationDAO;

    @Override
    public void init() {
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String resId = request.getParameter("id"); // e.g., OVR-2025-AE55
        
        if (resId == null || resId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Reservation ID.");
            return;
        }

        // Fetch single reservation details
        Reservation res = reservationDAO.getReservationByNumber(resId);

        if (res == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Reservation not found.");
            return;
        }

        request.setAttribute("res", res);
        
        // Forward to the printable JSP
        request.getRequestDispatcher("/invoice.jsp").forward(request, response);
    }
}
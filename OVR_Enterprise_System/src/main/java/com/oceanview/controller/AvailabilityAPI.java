package com.oceanview.controller;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.service.BillingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * This Servlet acts as a REST API endpoint.
 * It is called via JavaScript (AJAX) from the frontend when dates/room types are changed.
 * URL: /api/availability
 */
@WebServlet("/api/availability")
public class AvailabilityAPI extends HttpServlet {
    
    private RoomDAO roomDAO;
    private BillingService billingService;

    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
        billingService = new BillingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // --- DEBUG LOGS (Check Eclipse Console) ---
            System.out.println("[API] Availability Request Received.");
            
            String typeIdStr = request.getParameter("typeId");
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");
            
            System.out.println("[API] Params: Type=" + typeIdStr + ", In=" + checkInStr + ", Out=" + checkOutStr);

            if (typeIdStr == null || checkInStr == null || checkOutStr == null || 
                typeIdStr.isEmpty() || checkInStr.isEmpty() || checkOutStr.isEmpty()) {
                out.print("{\"error\": \"Missing parameters\"}");
                return;
            }

            int typeId = Integer.parseInt(typeIdStr);
            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);

            // Validate logic: Check-out must be after check-in
            if (!checkOut.isAfter(checkIn)) {
                out.print("{\"error\": \"Check-out date must be after check-in date.\"}");
                return;
            }

            // 3. Get Base Rate and Calculate Price
            RoomType roomType = roomDAO.getRoomTypeById(typeId);
            long nights = billingService.calculateNights(checkIn, checkOut);
            double totalCost = billingService.calculateTotalCost(checkIn, checkOut, roomType.getBaseRate());
            
            // Database Query
            List<Room> allRooms = roomDAO.getRoomStatusForRange(typeId, Date.valueOf(checkIn), Date.valueOf(checkOut));
            System.out.println("[API] Found " + allRooms.size() + " rooms.");

            // 5. Construct JSON Response manually (Strict Java EE constraint adherence)
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"nights\": ").append(nights).append(",");
            json.append("\"totalCost\": ").append(totalCost).append(",");
            json.append("\"rooms\": [");
            
            for (int i = 0; i < allRooms.size(); i++) {
                Room r = allRooms.get(i);
                json.append("{");
                json.append("\"id\": ").append(r.getRoomId()).append(",");
                json.append("\"number\": \"").append(r.getRoomNumber()).append("\",");
                json.append("\"status\": \"").append(r.getStatus()).append("\""); // AVAILABLE, BOOKED, or MAINTENANCE
                json.append("}");
                if (i < allRooms.size() - 1) json.append(",");
            }
            
            json.append("]");
            json.append("}");
            
            out.print(json.toString());

        } catch (Exception e) {
            e.printStackTrace(); // This prints the exact error in Console
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Server Error: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}
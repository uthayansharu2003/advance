package com.oceanview.dao;

import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public List<RoomType> getAllRoomTypes() {
        List<RoomType> types = new ArrayList<>();
        String sql = "SELECT * FROM room_types";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                RoomType type = new RoomType();
                type.setTypeId(rs.getInt("type_id"));
                type.setTypeName(rs.getString("type_name"));
                type.setBaseRate(rs.getDouble("base_rate"));
                types.add(type);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return types;
    }

    /**
     * Fetches ALL rooms of a type and determines their availability status 
     * for the requested date range.
     */
    public List<Room> getRoomStatusForRange(int typeId, Date checkIn, Date checkOut) {
        List<Room> rooms = new ArrayList<>();
        // This query checks if a reservation exists for the room in the given range
        String sql = "SELECT r.room_id, r.room_number, r.status AS base_status, " +
                     "(SELECT COUNT(*) FROM reservations res " +
                     " WHERE res.room_id = r.room_id " +
                     " AND res.status IN ('CONFIRMED', 'CHECKED_IN') " +
                     " AND res.check_in < ? AND res.check_out > ?) AS booking_count " +
                     "FROM rooms r WHERE r.type_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, checkOut); 
            stmt.setDate(2, checkIn);
            stmt.setInt(3, typeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("room_id"));
                    room.setRoomNumber(rs.getString("room_number"));
                    
                    // Determine display status
                    String baseStatus = rs.getString("base_status");
                    int bookingCount = rs.getInt("booking_count");
                    
                    if ("MAINTENANCE".equals(baseStatus)) {
                        room.setStatus("MAINTENANCE");
                    } else if (bookingCount > 0) {
                        room.setStatus("BOOKED");
                    } else {
                        room.setStatus("AVAILABLE");
                    }
                    rooms.add(room);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return rooms;
    }

    public RoomType getRoomTypeById(int typeId) {
        String sql = "SELECT * FROM room_types WHERE type_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    RoomType type = new RoomType();
                    type.setTypeId(rs.getInt("type_id"));
                    type.setTypeName(rs.getString("type_name"));
                    type.setBaseRate(rs.getDouble("base_rate"));
                    return type;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}
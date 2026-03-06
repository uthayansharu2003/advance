package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.model.ReportStats;
import com.oceanview.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public boolean createReservation(Reservation res) {
        String sql = "INSERT INTO reservations (reservation_no, guest_name, guest_address, " +
                     "guest_contact, room_id, check_in, check_out, total_cost, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'CONFIRMED')";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, res.getReservationNo());
            stmt.setString(2, res.getGuestName());
            stmt.setString(3, res.getGuestAddress());
            stmt.setString(4, res.getGuestContact());
            stmt.setInt(5, res.getRoomId());
            stmt.setDate(6, res.getCheckIn());
            stmt.setDate(7, res.getCheckOut());
            stmt.setDouble(8, res.getTotalCost());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false;
        }
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        // Fetching ALL columns + joining room_number
        String sql = "SELECT res.*, r.room_number FROM reservations res " +
                     "JOIN rooms r ON res.room_id = r.room_id " +
                     "ORDER BY res.created_at DESC";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setReservationNo(rs.getString("reservation_no"));
                res.setGuestName(rs.getString("guest_name"));
                res.setGuestContact(rs.getString("guest_contact"));
                res.setGuestAddress(rs.getString("guest_address")); // Added
                res.setRoomNumber(rs.getString("room_number"));
                res.setCheckIn(rs.getDate("check_in"));
                res.setCheckOut(rs.getDate("check_out"));
                res.setTotalCost(rs.getDouble("total_cost"));
                res.setStatus(rs.getString("status"));
                res.setCreatedAt(rs.getTimestamp("created_at")); // Added
                list.add(res);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public Reservation getReservationByNumber(String reservationNo) {
        String sql = "SELECT res.*, r.room_number FROM reservations res " +
                     "JOIN rooms r ON res.room_id = r.room_id WHERE res.reservation_no = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reservationNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Reservation res = new Reservation();
                    res.setReservationNo(rs.getString("reservation_no"));
                    res.setGuestName(rs.getString("guest_name"));
                    res.setGuestAddress(rs.getString("guest_address"));
                    res.setGuestContact(rs.getString("guest_contact"));
                    res.setCheckIn(rs.getDate("check_in"));
                    res.setCheckOut(rs.getDate("check_out"));
                    res.setTotalCost(rs.getDouble("total_cost"));
                    res.setRoomNumber(rs.getString("room_number"));
                    return res;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 1. Get Total Revenue
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_cost) FROM reservations WHERE status != 'CANCELLED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    // 2. Get Total Bookings Count
    public int getTotalBookingsCount() {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // 3. Get Revenue Grouped by Room Type
    public List<ReportStats> getRevenueByRoomType() {
        List<ReportStats> stats = new ArrayList<>();
        String sql = "SELECT rt.type_name, COUNT(res.reservation_id) AS bookings, SUM(res.total_cost) AS revenue " +
                     "FROM reservations res " +
                     "JOIN rooms r ON res.room_id = r.room_id " +
                     "JOIN room_types rt ON r.type_id = rt.type_id " +
                     "WHERE res.status != 'CANCELLED' " +
                     "GROUP BY rt.type_id, rt.type_name ORDER BY revenue DESC";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stats.add(new ReportStats(
                    rs.getString("type_name"),
                    rs.getInt("bookings"),
                    rs.getDouble("revenue") != 0 ? rs.getDouble("revenue") : 0.0
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }
}
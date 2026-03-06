package com.oceanview.model;

public class ReportStats {
    private String categoryName;
    private int totalBookings;
    private double totalRevenue;

    public ReportStats(String categoryName, int totalBookings, double totalRevenue) {
        this.categoryName = categoryName;
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
    }

    public String getCategoryName() { return categoryName; }
    public int getTotalBookings() { return totalBookings; }
    public double getTotalRevenue() { return totalRevenue; }
}
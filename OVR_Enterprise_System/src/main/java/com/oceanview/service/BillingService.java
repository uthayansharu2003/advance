package com.oceanview.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BillingService {
    
    // Constant for realistic hotel business logic (10% Tax/Service Charge)
    private static final double TAX_RATE = 0.10;

    /**
     * Calculates the number of nights between two dates.
     */
    public long calculateNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return 0; // Invalid dates
        }
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    /**
     * Calculates the total cost including taxes.
     * Demonstrates clear separation of business logic from the DAO/Database layer.
     */
    public double calculateTotalCost(LocalDate checkIn, LocalDate checkOut, double nightlyRate) {
        long nights = calculateNights(checkIn, checkOut);
        if (nights <= 0) {
            return 0.0;
        }
        
        double subTotal = nights * nightlyRate;
        double taxAmount = subTotal * TAX_RATE;
        double total = subTotal + taxAmount;
        
        // Round to 2 decimal places
        return Math.round(total * 100.0) / 100.0;
    }
}
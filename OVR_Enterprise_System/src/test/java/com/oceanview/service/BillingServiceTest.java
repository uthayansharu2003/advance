package com.oceanview.service;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.LocalDate;

public class BillingServiceTest {

    private BillingService billingService = new BillingService();

    @Test
    public void testCalculateNights_ValidDates() {
        LocalDate checkIn = LocalDate.of(2023, 10, 1);
        LocalDate checkOut = LocalDate.of(2023, 10, 5);
        long nights = billingService.calculateNights(checkIn, checkOut);
        assertEquals(4, nights);
    }

    @Test
    public void testCalculateNights_InvalidDates_CheckOutBeforeCheckIn() {
        LocalDate checkIn = LocalDate.of(2023, 10, 5);
        LocalDate checkOut = LocalDate.of(2023, 10, 1);
        long nights = billingService.calculateNights(checkIn, checkOut);
        assertEquals(0, nights);
    }

    @Test
    public void testCalculateNights_SameDate() {
        LocalDate checkIn = LocalDate.of(2023, 10, 1);
        LocalDate checkOut = LocalDate.of(2023, 10, 1);
        long nights = billingService.calculateNights(checkIn, checkOut);
        assertEquals(0, nights);
    }

    @Test
    public void testCalculateNights_NullDates() {
        long nights = billingService.calculateNights(null, LocalDate.of(2023, 10, 1));
        assertEquals(0, nights);
        nights = billingService.calculateNights(LocalDate.of(2023, 10, 1), null);
        assertEquals(0, nights);
    }

    @Test
    public void testCalculateTotalCost_Valid() {
        LocalDate checkIn = LocalDate.of(2023, 10, 1);
        LocalDate checkOut = LocalDate.of(2023, 10, 3);
        double nightlyRate = 100.0;
        double total = billingService.calculateTotalCost(checkIn, checkOut, nightlyRate);
        // 2 nights * 100 = 200, tax 20, total 220
        assertEquals(220.0, total, 0.01);
    }

    @Test
    public void testCalculateTotalCost_InvalidNights() {
        LocalDate checkIn = LocalDate.of(2023, 10, 1);
        LocalDate checkOut = LocalDate.of(2023, 9, 30);
        double nightlyRate = 100.0;
        double total = billingService.calculateTotalCost(checkIn, checkOut, nightlyRate);
        assertEquals(0.0, total, 0.01);
    }

    @Test
    public void testCalculateTotalCost_Rounding() {
        LocalDate checkIn = LocalDate.of(2023, 10, 1);
        LocalDate checkOut = LocalDate.of(2023, 10, 2);
        double nightlyRate = 99.99;
        double total = billingService.calculateTotalCost(checkIn, checkOut, nightlyRate);
        // 1 * 99.99 = 99.99, tax 9.999, total 109.989, rounded to 109.99
        assertEquals(109.99, total, 0.01);
    }
}
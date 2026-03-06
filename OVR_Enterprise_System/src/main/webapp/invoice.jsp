<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Invoice #${res.reservationNo}</title>
    <style>
        /* General Styles for Screen */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f1f5f9;
            display: flex;
            justify-content: center;
            padding: 40px;
        }
        .invoice-box {
            background: white;
            width: 210mm; /* A4 Width */
            min-height: 297mm; /* A4 Height */
            padding: 40px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            position: relative;
        }
        .header {
            display: flex;
            justify-content: space-between;
            border-bottom: 2px solid #0077b6;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        .logo h1 { margin: 0; color: #0077b6; }
        .logo p { margin: 5px 0 0; color: #64748b; font-size: 0.9rem; }
        .invoice-details { text-align: right; }
        .invoice-details h2 { margin: 0; color: #334155; }
        .invoice-details p { margin: 5px 0 0; color: #64748b; }

        .bill-to { margin-bottom: 30px; }
        .bill-to h3 { color: #0077b6; border-bottom: 1px solid #e2e8f0; padding-bottom: 5px; margin-bottom: 10px; }
        .bill-to p { margin: 3px 0; color: #334155; }

        table { width: 100%; border-collapse: collapse; margin-bottom: 30px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #e2e8f0; }
        th { background-color: #f8fafc; color: #334155; font-weight: bold; }
        .total-row td { font-weight: bold; font-size: 1.1rem; border-top: 2px solid #334155; }

        .footer {
            margin-top: 50px;
            text-align: center;
            font-size: 0.8rem;
            color: #94a3b8;
            border-top: 1px solid #e2e8f0;
            padding-top: 20px;
        }

        /* Print Button Styles */
        .no-print {
            position: fixed;
            top: 20px;
            right: 20px;
            background: #0077b6;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            font-weight: bold;
        }
        .no-print:hover { background: #0096c7; }

        /* Print-Specific Styles */
        @media print {
            body { background: white; padding: 0; }
            .invoice-box { box-shadow: none; width: 100%; height: auto; padding: 20px; }
            .no-print { display: none; } /* Hide button when printing */
        }
    </style>
</head>
<body>

    <button onclick="window.print()" class="no-print">🖨️ Print Invoice</button>

    <div class="invoice-box">
        <div class="header">
            <div class="logo">
                <h1>🌊 Ocean View Resort</h1>
                <p>123 Beach Road, Galle, Sri Lanka</p>
                <p>+94 77 123 4567 | info@oceanview.lk</p>
            </div>
            <div class="invoice-details">
                <h2>INVOICE</h2>
                <p><strong>Ref:</strong> ${res.reservationNo}</p>
                <p><strong>Date:</strong> <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd MMM yyyy" /></p>
                <p><strong>Status:</strong> ${res.status}</p>
            </div>
        </div>

        <div class="bill-to">
            <h3>Bill To:</h3>
            <p><strong>${res.guestName}</strong></p>
            <p>${res.guestAddress}</p>
            <p>Phone: ${res.guestContact}</p>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Description</th>
                    <th>Check In</th>
                    <th>Check Out</th>
                    <th style="text-align: right;">Amount</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Room Accommodation (${res.roomNumber})</td>
                    <td><fmt:formatDate value="${res.checkIn}" pattern="dd MMM yyyy"/></td>
                    <td><fmt:formatDate value="${res.checkOut}" pattern="dd MMM yyyy"/></td>
                    <td style="text-align: right;">
                        <!-- Basic logic: Total / 1.10 to reverse tax for display purposes -->
                        <fmt:formatNumber value="${res.totalCost / 1.10}" type="currency" currencySymbol="LKR " maxFractionDigits="2"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: right;">Service Charge & Tax (10%)</td>
                    <td style="text-align: right;">
                        <fmt:formatNumber value="${res.totalCost - (res.totalCost / 1.10)}" type="currency" currencySymbol="LKR " maxFractionDigits="2"/>
                    </td>
                </tr>
                <tr class="total-row">
                    <td colspan="3" style="text-align: right;">TOTAL PAYABLE</td>
                    <td style="text-align: right; color: #0077b6;">
                        <fmt:formatNumber value="${res.totalCost}" type="currency" currencySymbol="LKR " maxFractionDigits="2"/>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="footer">
            <p>Thank you for choosing Ocean View Resort!</p>
            <p>This is a computer-generated invoice. No signature required.</p>
        </div>
    </div>

</body>
</html>
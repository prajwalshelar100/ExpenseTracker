package com.mycompany.expensetracker.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DataValidator {
    public static boolean isValidAmount(String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidDescription(String description) {
        return description != null && !description.trim().isEmpty();
    }

    public static boolean isValidCategory(String category) {
        return category != null && !category.trim().isEmpty();
    }

    public static boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null && !paymentMethod.trim().isEmpty();
    }

    public static String formatAmount(double amount) {
        return String.format("$%.2f", amount);
    }

    public static String formatPercentage(double percentage) {
        return String.format("%.1f%%", percentage);
    }
} 
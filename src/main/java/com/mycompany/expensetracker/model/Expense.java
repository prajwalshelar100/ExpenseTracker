package com.mycompany.expensetracker.model;

import java.time.LocalDate;

public class Expense {
    private String description;
    private double amount;
    private String category;
    private LocalDate date;
    private String paymentMethod;

    public Expense(String description, double amount, String category, LocalDate date, String paymentMethod) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        return String.format("%s - $%.2f (%s) [%s] - %s", 
            description, amount, category, date.toString(), paymentMethod);
    }
} 
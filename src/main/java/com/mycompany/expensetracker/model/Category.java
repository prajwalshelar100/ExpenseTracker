package com.mycompany.expensetracker.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private double budget;
    private List<Expense> expenses;

    public Category(String name, double budget) {
        this.name = name;
        this.budget = budget;
        this.expenses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public double getTotalExpenses() {
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double getRemainingBudget() {
        return budget - getTotalExpenses();
    }

    public double getBudgetUtilization() {
        if (budget == 0) return 0;
        return (getTotalExpenses() / budget) * 100;
    }
} 
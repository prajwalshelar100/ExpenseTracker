/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.expensetrackerui;

/**
 *
 * @author prajw
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Expense class represents an individual expense with description, amount, and category
class Expense {
    private String description;
    private double amount;
    private String category;

    public Expense(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
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
}

// ExpenseTracker class manages expenses by category, adding, clearing, and retrieving expenses
class ExpenseTracker {
    private Map<String, List<Expense>> expensesByCategory;

    public ExpenseTracker() {
        expensesByCategory = new HashMap<>();
    }

    // Add an expense to the specified category
    public void addExpense(String description, double amount, String category) {
        Expense expense = new Expense(description, amount, category);

        if (!expensesByCategory.containsKey(category)) {
            expensesByCategory.put(category, new ArrayList<>());
        }
        expensesByCategory.get(category).add(expense);
    }

    // Clear all recorded expenses
    public void clearExpenses() {
        expensesByCategory.clear();
    }

    // Get all recorded expenses categorized by expense category
    public Map<String, List<Expense>> getExpensesByCategory() {
        return expensesByCategory;
    }

    // Calculate total expenses for a specific category
    public double getTotalExpensesForCategory(String category) {
        if (expensesByCategory.containsKey(category)) {
            List<Expense> expenses = expensesByCategory.get(category);
            return expenses.stream().mapToDouble(Expense::getAmount).sum();
        }
        return 0;
    }

    // Get description of the last 5 recorded expenses
    public String getRecentExpensesDescription() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (List<Expense> expenseList : expensesByCategory.values()) {
            for (Expense expense : expenseList) {
                sb.append("Description: ").append(expense.getDescription())
                        .append(", Amount: ").append(expense.getAmount())
                        .append(", Category: ").append(expense.getCategory()).append("\n");
                count++;
                if (count >= 5) { // Show only the last 5 expenses
                    return sb.toString();
                }
            }
        }
        return sb.toString();
    }
}

// ExpenseTrackerUI class implements the graphical user interface using Swing components
public class ExpenseTrackerUI extends JFrame {

    private ExpenseTracker tracker;
    private JTextArea recentExpensesTextArea;

    public ExpenseTrackerUI() {
        tracker = new ExpenseTracker();

        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for buttons and their actions
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        // Button to add a new expense
        JButton addExpenseBtn = new JButton("Add Expense");
        addExpenseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = JOptionPane.showInputDialog("Enter expense description:");
                double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter expense amount:"));
                String category = JOptionPane.showInputDialog("Enter expense category:");

                tracker.addExpense(description, amount, category);
                JOptionPane.showMessageDialog(null, "Expense added successfully!");
                updateRecentExpensesTextArea();
            }
        });
        topPanel.add(addExpenseBtn);

        // Button to view all recorded expenses
        JButton viewExpensesBtn = new JButton("View Expenses");
        viewExpensesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, List<Expense>> expenses = tracker.getExpensesByCategory();
                if (expenses.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No expenses recorded yet.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String category : expenses.keySet()) {
                        sb.append("Category: ").append(category).append("\n");
                        List<Expense> categoryExpenses = expenses.get(category);
                        for (Expense expense : categoryExpenses) {
                            sb.append("Description: ").append(expense.getDescription())
                                    .append(", Amount: ").append(expense.getAmount())
                                    .append(", Category: ").append(expense.getCategory()).append("\n");
                        }
                        sb.append("\n");
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());
                }
            }
        });
        topPanel.add(viewExpensesBtn);

        // Button to get expense summary for a category
        JButton expenseSummaryBtn = new JButton("Expense Summary");
        expenseSummaryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = JOptionPane.showInputDialog("Enter category for expense summary:");
                double total = tracker.getTotalExpensesForCategory(category);
                if (total > 0) {
                    JOptionPane.showMessageDialog(null, "Total expenses for category " + category + ": " + total);
                } else {
                    JOptionPane.showMessageDialog(null, "No expenses recorded for this category.");
                }
            }
        });
        topPanel.add(expenseSummaryBtn);

        // Button to clear all recorded expenses
        JButton clearExpensesBtn = new JButton("Clear Expenses");
        clearExpensesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tracker.clearExpenses();
                JOptionPane.showMessageDialog(null, "All expenses cleared!");
                updateRecentExpensesTextArea();
            }
        });
        topPanel.add(clearExpensesBtn);

        add(topPanel, BorderLayout.NORTH); // Add buttons panel to the top of the frame

        // Text area to display recent expenses
        recentExpensesTextArea = new JTextArea(10, 30);
        recentExpensesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(recentExpensesTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        updateRecentExpensesTextArea(); // Initial update
        add(scrollPane, BorderLayout.CENTER); // Add text area to the center of the frame

        // Button to exit the application
        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tracker = null; // Release resources
                System.exit(0); // Close the application
            }
        });
        add(exitBtn, BorderLayout.SOUTH); // Add exit button to the bottom of the frame

        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    // Update the text area with the description of the recent expenses
    private void updateRecentExpensesTextArea() {
        recentExpensesTextArea.setText(tracker.getRecentExpensesDescription());
    }

    // Main method to start the Expense Tracker UI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExpenseTrackerUI(); // Create and display the Expense Tracker UI
            }
        });
    }
}

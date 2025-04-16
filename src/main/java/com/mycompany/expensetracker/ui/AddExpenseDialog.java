package com.mycompany.expensetracker.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mycompany.expensetracker.model.Expense;
import com.mycompany.expensetracker.util.DataValidator;

public class AddExpenseDialog extends JDialog {
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<String> categoryComboBox;
    private JTextField dateField;
    private JComboBox<String> paymentMethodComboBox;
    private boolean confirmed = false;
    private Expense expense;

    public AddExpenseDialog(JFrame parent, String[] categories) {
        super(parent, "Add New Expense", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Description
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        add(descriptionField, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        amountField = new JTextField(20);
        add(amountField, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryComboBox = new JComboBox<>(categories);
        add(categoryComboBox, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        add(dateField, gbc);

        // Payment Method
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        String[] paymentMethods = {"Cash", "Credit Card", "Debit Card", "Bank Transfer", "Other"};
        paymentMethodComboBox = new JComboBox<>(paymentMethods);
        add(paymentMethodComboBox, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                createExpense();
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    private boolean validateInput() {
        if (!DataValidator.isValidDescription(descriptionField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid description", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!DataValidator.isValidAmount(amountField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!DataValidator.isValidDate(dateField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date (YYYY-MM-DD)", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void createExpense() {
        String description = descriptionField.getText();
        double amount = Double.parseDouble(amountField.getText());
        String category = (String) categoryComboBox.getSelectedItem();
        LocalDate date = LocalDate.parse(dateField.getText());
        String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

        expense = new Expense(description, amount, category, date, paymentMethod);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Expense getExpense() {
        return expense;
    }
} 
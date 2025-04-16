package com.mycompany.expensetracker.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.mycompany.expensetracker.model.Category;
import com.mycompany.expensetracker.model.Expense;
import com.mycompany.expensetracker.util.DataValidator;
import com.mycompany.expensetracker.util.FileManager;

public class MainFrame extends JFrame {
    private List<Expense> expenses;
    private Map<String, Category> categories;
    private DefaultTableModel expenseTableModel;
    private JTable expenseTable;
    private JLabel totalExpensesLabel;
    private JLabel budgetStatusLabel;

    public MainFrame() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize data
        expenses = FileManager.loadExpenses();
        categories = FileManager.loadCategories();
        if (categories.isEmpty()) {
            initializeDefaultCategories();
        }

        // Create menu bar
        createMenuBar();

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create expense table
        createExpenseTable();
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Create control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Expense");
        JButton clearButton = new JButton("Clear All");
        JButton exportButton = new JButton("Export to CSV");

        addButton.addActionListener(e -> addExpense());
        clearButton.addActionListener(e -> clearExpenses());
        exportButton.addActionListener(e -> exportToCSV());

        controlPanel.add(addButton);
        controlPanel.add(clearButton);
        controlPanel.add(exportButton);
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Create status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalExpensesLabel = new JLabel("Total Expenses: $0.00");
        budgetStatusLabel = new JLabel("Budget Status: ");
        statusPanel.add(totalExpensesLabel);
        statusPanel.add(budgetStatusLabel);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");

        // File menu items
        JMenuItem exportItem = new JMenuItem("Export to CSV");
        JMenuItem exitItem = new JMenuItem("Exit");

        exportItem.addActionListener(e -> exportToCSV());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Edit menu items
        JMenuItem addExpenseItem = new JMenuItem("Add Expense");
        JMenuItem clearAllItem = new JMenuItem("Clear All");

        addExpenseItem.addActionListener(e -> addExpense());
        clearAllItem.addActionListener(e -> clearExpenses());

        editMenu.add(addExpenseItem);
        editMenu.add(clearAllItem);

        // View menu items
        JMenuItem refreshItem = new JMenuItem("Refresh");
        JMenuItem filterItem = new JMenuItem("Filter by Category");

        refreshItem.addActionListener(e -> updateUI());
        filterItem.addActionListener(e -> filterByCategory());

        viewMenu.add(refreshItem);
        viewMenu.add(filterItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);
    }

    private void createExpenseTable() {
        String[] columnNames = {"Date", "Description", "Amount", "Category", "Payment Method"};
        expenseTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expenseTable = new JTable(expenseTableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.getTableHeader().setReorderingAllowed(false);
    }

    private void initializeDefaultCategories() {
        String[] defaultCategories = {
            "Food", "Transportation", "Housing", "Utilities",
            "Entertainment", "Shopping", "Healthcare", "Education"
        };

        for (String category : defaultCategories) {
            categories.put(category, new Category(category, 0.0));
        }
        FileManager.saveCategories(categories);
    }

    private void addExpense() {
        AddExpenseDialog dialog = new AddExpenseDialog(this, categories.keySet().toArray(new String[0]));
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Expense expense = dialog.getExpense();
            expenses.add(expense);
            categories.get(expense.getCategory()).addExpense(expense);
            FileManager.saveExpenses(expenses);
            FileManager.saveCategories(categories);
            updateUI();
        }
    }

    private void clearExpenses() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear all expenses?",
            "Confirm Clear",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            expenses.clear();
            categories.values().forEach(category -> category.getExpenses().clear());
            FileManager.saveExpenses(expenses);
            FileManager.saveCategories(categories);
            updateUI();
        }
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");
        fileChooser.setSelectedFile(new File("expenses.csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            FileManager.exportToCSV(expenses, file.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Expenses exported successfully!");
        }
    }

    private void filterByCategory() {
        String[] categoryArray = categories.keySet().toArray(new String[0]);
        String selectedCategory = (String) JOptionPane.showInputDialog(
            this,
            "Select a category:",
            "Filter by Category",
            JOptionPane.QUESTION_MESSAGE,
            null,
            categoryArray,
            categoryArray[0]
        );

        if (selectedCategory != null) {
            updateUI(selectedCategory);
        }
    }

    private void updateUI() {
        updateUI(null);
    }

    private void updateUI(String filterCategory) {
        expenseTableModel.setRowCount(0);
        double totalExpenses = 0;

        for (Expense expense : expenses) {
            if (filterCategory == null || expense.getCategory().equals(filterCategory)) {
                expenseTableModel.addRow(new Object[]{
                    expense.getDate().toString(),
                    expense.getDescription(),
                    DataValidator.formatAmount(expense.getAmount()),
                    expense.getCategory(),
                    expense.getPaymentMethod()
                });
                totalExpenses += expense.getAmount();
            }
        }

        totalExpensesLabel.setText("Total Expenses: " + DataValidator.formatAmount(totalExpenses));
        updateBudgetStatus();
    }

    private void updateBudgetStatus() {
        StringBuilder status = new StringBuilder("Budget Status: ");
        for (Category category : categories.values()) {
            if (category.getBudget() > 0) {
                status.append(String.format("%s: %s used, ",
                    category.getName(),
                    DataValidator.formatPercentage(category.getBudgetUtilization())));
            }
        }
        budgetStatusLabel.setText(status.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
} 
package com.mycompany.expensetracker.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.expensetracker.model.Category;
import com.mycompany.expensetracker.model.Expense;

public class FileManager {
    private static final String EXPENSES_FILE = "expenses.dat";
    private static final String CATEGORIES_FILE = "categories.dat";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void saveExpenses(List<Expense> expenses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EXPENSES_FILE))) {
            oos.writeObject(expenses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EXPENSES_FILE))) {
            expenses = (List<Expense>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File doesn't exist or is empty, return empty list
        }
        return expenses;
    }

    public static void saveCategories(Map<String, Category> categories) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CATEGORIES_FILE))) {
            oos.writeObject(categories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Category> loadCategories() {
        Map<String, Category> categories = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CATEGORIES_FILE))) {
            categories = (Map<String, Category>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File doesn't exist or is empty, return empty map
        }
        return categories;
    }

    public static void exportToCSV(List<Expense> expenses, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Date,Description,Amount,Category,Payment Method");
            for (Expense expense : expenses) {
                writer.println(String.format("%s,%s,%.2f,%s,%s",
                    expense.getDate().format(DATE_FORMATTER),
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getPaymentMethod()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 
package expensetrack;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseManager {
    private final List<Expense> expenses = new ArrayList<>();
    private static final String FILE_NAME = "expenses.txt";

    public ExpenseManager() {
        loadExpenses();
    }

    public void addExpense(Expense e) {
        expenses.add(e);
        saveExpenses();
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            System.out.printf("%d. %s | %s | ₹%.2f | %s%n", i, e.getDate(), e.getCategory(), e.getAmount(), e.getDescription());
        }
    }

    public double getTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public void showCategorySummary() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to summarize.");
            return;
        }
        System.out.println("\n-- Category-wise Summary --");
        expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategory, TreeMap::new, Collectors.summingDouble(Expense::getAmount)))
                .forEach((cat, amt) -> System.out.printf("%s: ₹%.2f%n", cat, amt));
    }

    public void exportToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("Date,Category,Amount,Description");
            expenses.forEach(e -> writer.printf("%s,%s,%.2f,%s%n", e.getDate(), e.getCategory(), e.getAmount(), e.getDescription()));
            System.out.println("✅ Expenses exported to " + filename);
        } catch (IOException e) {
            System.out.println("❌ Error exporting: " + e.getMessage());
        }
    }

    public void deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            Expense removed = expenses.remove(index);
            saveExpenses();
            System.out.printf("✅ Deleted: %s | ₹%.2f%n", removed.getCategory(), removed.getAmount());
        } else {
            System.out.println("❌ Invalid index. No expense deleted.");
        }
    }

    private void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            expenses.forEach(e -> writer.printf("%s,%s,%.2f,%s%n", e.getDate(), e.getCategory(), e.getAmount(), e.getDescription()));
        } catch (IOException e) {
            System.out.println("❌ Error saving expenses: " + e.getMessage());
        }
    }

    private void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",", 4);
                if (p.length == 4)
                    expenses.add(new Expense(p[1], Double.parseDouble(p[2]), LocalDate.parse(p[0]), p[3]));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("❌ Error loading expenses: " + e.getMessage());
        }
    }
}

package expensetrack;

import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.print("""
                \n====== Expense Tracker ======
                1. Add Expense
                2. View Expenses
                3. View Total Expenses
                4. View Category-wise Summary
                5. Export to CSV
                6. Delete Expense
                7. Exit
                Enter your choice: """);

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    manager.addExpense(new Expense(category, amount, LocalDate.now(), description));
                    System.out.println("✅ Expense added!");
                }
                case 2 -> {
                    System.out.println("\n-- All Expenses --");
                    manager.viewExpenses();
                }
                case 3 -> System.out.printf("Total Expenses: ₹%.2f\n", manager.getTotalExpenses());
                case 4 -> manager.showCategorySummary();
                case 5 -> {
                    System.out.print("Enter file name (e.g., expenses.csv): ");
                    manager.exportToCSV(scanner.nextLine());
                }
                case 6 -> {
                    manager.viewExpenses();
                    System.out.print("Enter index to delete: ");
                    manager.deleteExpense(scanner.nextInt());
                    scanner.nextLine();
                }
                case 7 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 7);

        scanner.close();
    }
}

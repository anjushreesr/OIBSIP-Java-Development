import java.util.ArrayList;
import java.util.Scanner;

/**
 * ATM handles everything the user sees: login, the menu and each operation.
 * It talks to Bank (to find accounts) and Account (to change balances).
 */
public class ATM {

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private final Bank bank;
    private final Scanner scanner = new Scanner(System.in);
    private Account currentAccount; // the logged-in customer

    public ATM(Bank bank) {
        this.bank = bank;
    }

    // =====================================================
    // Start the ATM session
    // =====================================================
    public void start() {
        System.out.println("=====================================");
        System.out.println("          WELCOME TO THE ATM         ");
        System.out.println("=====================================");

        if (login()) {
            runMenu();
        }
        System.out.println();
        System.out.println("Thank you for using the ATM. Goodbye!");
    }

    // =====================================================
    // Login (maximum 3 attempts)
    // =====================================================
    private boolean login() {
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            String userId = readLine("Enter User ID: ").trim();
            String pin = readLine("Enter PIN: ").trim();

            Account account = bank.authenticate(userId, pin);
            if (account != null) {
                currentAccount = account;
                System.out.println();
                System.out.println("Login successful. Welcome, " + account.getHolderName() + "!");
                return true;
            }

            int attemptsLeft = MAX_LOGIN_ATTEMPTS - attempt;
            if (attemptsLeft > 0) {
                System.out.println("Error: Invalid User ID or PIN. Attempts remaining: " + attemptsLeft);
                System.out.println();
            } else {
                System.out.println("Error: Too many incorrect attempts. Access denied.");
            }
        }
        return false;
    }

    // =====================================================
    // Main menu loop
    // =====================================================
    private void runMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readMenuChoice();
            System.out.println();

            switch (choice) {
                case 1:
                    showTransactionHistory();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    System.out.println("Logged out successfully.");
                    currentAccount = null;
                    running = false;
                    break;
                default:
                    System.out.println("Error: Please choose a number from 1 to 5.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("-------------- ATM MENU --------------");
        System.out.println(String.format("Current balance: $%.2f", currentAccount.getBalance()));
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit (Logout)");
        System.out.println("--------------------------------------");
    }

    // =====================================================
    // Menu operations
    // =====================================================
    private void showTransactionHistory() {
        ArrayList<Transaction> history = currentAccount.getTransactionHistory();
        System.out.println("============ TRANSACTION HISTORY ============");
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println(Transaction.getTableHeader());
            for (Transaction transaction : history) {
                System.out.println(transaction);
            }
        }
        System.out.println(String.format("Current balance: $%.2f", currentAccount.getBalance()));
    }

    private void withdraw() {
        Double amount = readAmount("Enter amount to withdraw: $");
        if (amount == null) {
            return; // invalid input, message already shown
        }
        try {
            currentAccount.withdraw(amount);
            System.out.println(String.format("Success: $%.2f withdrawn.", amount));
            System.out.println(String.format("Updated balance: $%.2f", currentAccount.getBalance()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deposit() {
        Double amount = readAmount("Enter amount to deposit: $");
        if (amount == null) {
            return;
        }
        try {
            currentAccount.deposit(amount);
            System.out.println(String.format("Success: $%.2f deposited.", amount));
            System.out.println(String.format("Updated balance: $%.2f", currentAccount.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void transfer() {
        String recipientId = readLine("Enter recipient User ID: ").trim();
        Account recipient = bank.findAccount(recipientId);

        if (recipient == null) {
            System.out.println("Error: No account found with User ID \"" + recipientId + "\".");
            return;
        }
        if (recipient == currentAccount) {
            System.out.println("Error: You cannot transfer money to your own account.");
            return;
        }

        Double amount = readAmount("Enter amount to transfer: $");
        if (amount == null) {
            return;
        }
        try {
            currentAccount.transferTo(recipient, amount);
            System.out.println(String.format("Success: $%.2f transferred to %s (%s).",
                    amount, recipient.getHolderName(), recipient.getUserId()));
            System.out.println(String.format("Updated balance: $%.2f", currentAccount.getBalance()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // =====================================================
    // Input helpers
    // =====================================================

    /** Reads a menu number; returns -1 if the input is not a number. */
    private int readMenuChoice() {
        String input = readLine("Choose an option (1-5): ").trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // handled by the "default" case in the menu
        }
    }

    /** Reads an amount; returns null (and shows an error) if it is not a number. */
    private Double readAmount(String prompt) {
        String input = readLine(prompt).trim();
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid numeric amount.");
            return null;
        }
    }

    /** Reads a line of text. If input ends (Ctrl+D), exit cleanly. */
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("Input closed. Exiting ATM. Goodbye!");
            System.exit(0);
        }
        return scanner.nextLine();
    }
}
import java.util.ArrayList;

/**
 * Account stores one customer's details, balance and transaction history.
 * Fields are private (encapsulation): other classes can only change the
 * balance through deposit(), withdraw() and transferTo(), which validate input.
 */
public class Account {

    private final String userId;
    private final String pin;
    private final String holderName;
    private double balance;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    public Account(String userId, String pin, String holderName, double openingBalance) {
        this.userId = userId;
        this.pin = pin;
        this.holderName = holderName;
        this.balance = openingBalance;
        // The first history entry shows how the account started
        transactions.add(new Transaction(Transaction.OPENING_BALANCE,
                openingBalance, balance, "Account opened"));
    }

    // ----- Getters -----
    public String getUserId() {
        return userId;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    /** Returns a COPY of the history so outside code cannot edit it. */
    public ArrayList<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    /** Checks whether the entered PIN is correct. */
    public boolean verifyPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    // ----- Banking operations -----

    /** Adds money to the account. */
    public void deposit(double amount) {
        validateAmount(amount);
        balance += amount;
        transactions.add(new Transaction(Transaction.DEPOSIT, amount, balance, "Cash deposit"));
    }

    /** Removes money from the account if there is enough balance. */
    public void withdraw(double amount) {
        validateAmount(amount);
        checkSufficientBalance(amount);
        balance -= amount;
        transactions.add(new Transaction(Transaction.WITHDRAW, amount, balance, "Cash withdrawal"));
    }

    /** Moves money from this account to another account. */
    public void transferTo(Account recipient, double amount) {
        if (recipient == null) {
            throw new IllegalArgumentException("Recipient account does not exist.");
        }
        if (recipient == this) {
            throw new IllegalArgumentException("You cannot transfer money to your own account.");
        }
        validateAmount(amount);
        checkSufficientBalance(amount);

        balance -= amount;
        transactions.add(new Transaction(Transaction.TRANSFER_OUT, amount, balance,
                "Transfer to " + recipient.getUserId()));
        recipient.receiveTransfer(amount, userId);
    }

    /** Called by transferTo() on the receiving account. */
    private void receiveTransfer(double amount, String senderId) {
        balance += amount;
        transactions.add(new Transaction(Transaction.TRANSFER_IN, amount, balance,
                "Transfer from " + senderId));
    }

    // ----- Helper (validation) methods -----

    /** Rejects zero, negative, NaN, infinite or more-than-2-decimal amounts. */
    private void validateAmount(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("Invalid amount.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        // Money has at most 2 decimal places (cents)
        if (Math.abs(amount * 100 - Math.round(amount * 100)) > 0.000001) {
            throw new IllegalArgumentException("Amount can have at most 2 decimal places.");
        }
    }

    private void checkSufficientBalance(double amount) {
        if (amount > balance) {
            throw new IllegalStateException(
                    String.format("Insufficient balance. Your balance is $%.2f.", balance));
        }
    }
}
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction represents ONE entry in an account's history
 * (a deposit, a withdrawal, a transfer, etc.).
 * Once created it can never be changed (all fields are final).
 */
public class Transaction {

    // Transaction type names used across the project
    public static final String OPENING_BALANCE = "OPENING BALANCE";
    public static final String DEPOSIT = "DEPOSIT";
    public static final String WITHDRAW = "WITHDRAW";
    public static final String TRANSFER_OUT = "TRANSFER OUT";
    public static final String TRANSFER_IN = "TRANSFER IN";

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String type;
    private final double amount;
    private final double balanceAfter;
    private final String description;
    private final LocalDateTime timestamp;

    public Transaction(String type, double amount, double balanceAfter, String description) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    // ----- Getters (fields are private, so we expose them safely) -----
    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /** Header line printed above the transaction list. */
    public static String getTableHeader() {
        return String.format("%-20s %-16s %12s %12s  %s",
                "Date & Time", "Type", "Amount", "Balance", "Details");
    }

    /** One formatted row of the transaction table. */
    @Override
    public String toString() {
        return String.format("%-20s %-16s %12s %12s  %s",
                timestamp.format(TIME_FORMAT),
                type,
                String.format("$%.2f", amount),
                String.format("$%.2f", balanceAfter),
                description);
    }
}
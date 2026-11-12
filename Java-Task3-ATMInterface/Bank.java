import java.util.HashMap;

/**
 * Bank keeps all customer accounts and checks login details.
 * (A HashMap is used as a simple in-memory "database".)
 */
public final class Bank {

    // Key = User ID, Value = Account
    private final HashMap<String, Account> accounts = new HashMap<>();

    public Bank() {
        loadSampleAccounts();
    }

    /** Creates test accounts so the program can be tried immediately. */
    private void loadSampleAccounts() {
        addAccount(new Account("1001", "1234", "Alice Johnson", 5000.00));
        addAccount(new Account("1002", "4321", "Bob Smith", 3000.00));
        addAccount(new Account("1003", "1111", "Charlie Brown", 10000.00));
    }

    public void addAccount(Account account) {
        accounts.put(account.getUserId(), account);
    }

    /** Finds an account by User ID (returns null if not found). */
    public Account findAccount(String userId) {
        return accounts.get(userId);
    }

    /**
     * Returns the Account if the User ID and PIN match,
     * otherwise returns null.
     */
    public Account authenticate(String userId, String pin) {
        Account account = accounts.get(userId);
        if (account != null && account.verifyPin(pin)) {
            return account;
        }
        return null;
    }
}
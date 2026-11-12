/**
 * Main is the entry point of the ATM Interface project.
 * It creates the Bank (with sample accounts) and starts the ATM.
 */
public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        ATM atm = new ATM(bank);
        atm.start();
    }
}
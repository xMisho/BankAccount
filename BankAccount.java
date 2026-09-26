public class BankAccount {

    private int accountID;
    private String ownerName;
    private double balance;

    public BankAccount(int accountID, String ownerName, double balance) {
        this.accountID = accountID;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public int getAccountID() {
        return accountID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            } 
        else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;    
            } 
        else {
            System.out.println("Insufficient funds or invalid withdrawal amount.");
            return false;
        }
    }

    


}
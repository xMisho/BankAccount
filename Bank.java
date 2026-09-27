import java.util.concurrent.ConcurrentHashMap;

public final class Bank {

    private final ConcurrentHashMap<Integer, BankAccount> accounts;

    public Bank() {
        accounts = new ConcurrentHashMap<>();
    }

    public boolean addAccount(BankAccount account) {
        BankAccount existing = accounts.putIfAbsent(account.getAccountID(), account);

        if (existing != null) {
            System.out.println("Account with ID " + account.getAccountID() + " already exists.");
            return false;
        }
        return true;
    }

    public BankAccount getAccount(int accountID) {
        return accounts.get(accountID);
    }

    public boolean accountExists(int accountID) {
        if (accounts.containsKey(accountID)) {
            return true;
        } else {
            System.out.println("Account with ID " + accountID + " does not exist.");
            return false;
        }
    }       

    public boolean removeAccount(int accountID) {
        if (accounts.remove(accountID) != null) {
            return true;
        } else {
            System.out.println("Account with ID " + accountID + " does not exist.");
            return false;
        }
    }

    public void displayAllAccounts() {
        for (BankAccount account : accounts.values()) {
            System.out.println("Account ID: " + account.getAccountID() +
                    ", Owner: " + account.getOwnerName() +
                    ", Balance: " + account.getBalance());
        }
    }

    public boolean transfer(int fromAccountID, int toAccountID, double amount) {
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return false;
        }

        if (fromAccountID == toAccountID) {
            System.out.println("Cannot transfer to the same account.");
            return false;
        }

        BankAccount fromAccount = accounts.get(fromAccountID);
        BankAccount toAccount = accounts.get(toAccountID);

        if (fromAccount == null || toAccount == null) {
            System.out.println("One or both accounts do not exist.");
            return false;
        }

        BankAccount firstAccount;
        BankAccount secondAccount;

        if (fromAccountID < toAccountID) {
        firstAccount = fromAccount;
        secondAccount = toAccount;
        } else  
        {
        firstAccount = toAccount;
        secondAccount = fromAccount;
        }

        synchronized (firstAccount) {
            synchronized (secondAccount) {
                if (fromAccount.withdraw(amount)) {
                    toAccount.deposit(amount);
                    return true;
                }

                System.out.println("Transfer failed due to insufficient funds.");
                return false;
            }
        }
    }
}



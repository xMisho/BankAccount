import java.util.concurrent.ConcurrentHashMap;

public class Bank {

    private ConcurrentHashMap<Integer, BankAccount> accounts;

    public Bank() {
        accounts = new ConcurrentHashMap<>();
    }

    public boolean addAccount(BankAccount account) {
    
        if (accounts.containsKey(account.getAccountID())) {
            System.out.println("Account with ID " + account.getAccountID() + " already exists.");
            return false;
        } else {
            accounts.put(account.getAccountID(), account);
            return true;
        }
}

    public BankAccount getAccount(int accountID) {
        return accounts.get(accountID);
    }


}
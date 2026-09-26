public class Main {

    public static void main(String[] args) {

        Bank bank = new Bank();

        BankAccount account1 =
                new BankAccount(1, "Misho", 1000);

        BankAccount account2 =
                new BankAccount(2, "Sara", 2000);

        bank.addAccount(account1);
        bank.addAccount(account2);

        BankAccount foundAccount = bank.getAccount(1);

        System.out.println(
                "Owner: " + foundAccount.getOwnerName()
        );

        System.out.println(
                "Balance: " + foundAccount.getBalance()
        );
    }
}
public class Main {

    public static void main(String[] args) throws InterruptedException {

        Bank bank = new Bank();

        BankAccount account1 =
                new BankAccount(1, "Misho", 1000);

        BankAccount account2 =
                new BankAccount(2, "Sara", 1000);

        bank.addAccount(account1);
        bank.addAccount(account2);

        System.out.println("Starting balances:");
        System.out.println("Misho: " + account1.getBalance());
        System.out.println("Sara: " + account2.getBalance());

        System.out.println("\nManual transfers:");

        bank.transfer(1, 2, 300);
        bank.transfer(2, 1, 50);

        Thread thread1 = new Thread(() -> {

            for (int i = 0; i < 5; i++) {
                bank.transfer(1, 2, 10);
            }

        });

        Thread thread2 = new Thread(() -> {

            for (int i = 0; i < 5; i++) {
                bank.transfer(2, 1, 10);
            }

        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("\nFinal balances:");
        System.out.println("Misho: " + account1.getBalance());
        System.out.println("Sara: " + account2.getBalance());

        System.out.println("\nTransaction history:");
        bank.displayTransactionHistory();
    }
}
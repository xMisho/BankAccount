public class Main {

    public static void main(String[] args) throws InterruptedException {

        Bank bank = new Bank();

        BankAccount account1 =
                new BankAccount(1, "Misho", 200_000);

        BankAccount account2 =
                new BankAccount(2, "Sara", 200_000);

        bank.addAccount(account1);
        bank.addAccount(account2);

        Thread thread1 = new Thread(() -> {

            for (int i = 0; i < 100_000; i++) {
                bank.transfer(1, 2, 1);
            }

        });

        Thread thread2 = new Thread(() -> {

            for (int i = 0; i < 100_000; i++) {
                bank.transfer(2, 1, 1);
            }

        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(
                "Account 1 balance: " + account1.getBalance()
        );

        System.out.println(
                "Account 2 balance: " + account2.getBalance()
        );

        double total =
                account1.getBalance() + account2.getBalance();

        System.out.println(
                "Total money: " + total
        );
    }
}
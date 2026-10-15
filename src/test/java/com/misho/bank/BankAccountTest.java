package com.misho.bank;

@org.junit.jupiter.api.Timeout(30)
class BankAccountTest {

    @org.junit.jupiter.api.Test
    void concurrentTransfersPreserveBalances() throws InterruptedException {

        Bank bank = new Bank();

        BankAccount account1 =
                new BankAccount(1, "Mike", 100_000);

        BankAccount account2 =
                new BankAccount(2, "John", 100_000);

        bank.addAccount(account1);
        bank.addAccount(account2);

        Thread depositThread = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                bank.deposit(1, 1);
            }

        });

        Thread withdrawThread = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                bank.withdraw(2, 1);
            }

        });

        Thread transferThread1 = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                bank.transfer(1, 2, 1);
            }

        });

        Thread transferThread2 = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                bank.transfer(2, 1, 1);
            }

        });

        java.util.concurrent.atomic.AtomicReference<Throwable> failure =
                new java.util.concurrent.atomic.AtomicReference<>();
        for (Thread worker : new Thread[]{depositThread, withdrawThread, transferThread1, transferThread2}) {
            worker.setUncaughtExceptionHandler((thread, error) -> failure.compareAndSet(null, error));
        }
        depositThread.start();
        withdrawThread.start();
        transferThread1.start();
        transferThread2.start();

        depositThread.join();
        withdrawThread.join();
        transferThread1.join();
        transferThread2.join();

        if (failure.get() != null) {
            throw new AssertionError("Worker thread failed", failure.get());
        }
        org.junit.jupiter.api.Assertions.assertEquals(110_000, account1.getBalance());
        org.junit.jupiter.api.Assertions.assertEquals(90_000, account2.getBalance());
        org.junit.jupiter.api.Assertions.assertEquals(200_000, account1.getBalance() + account2.getBalance());
        org.junit.jupiter.api.Assertions.assertEquals(40_000, bank.getTransactionCount());
        org.junit.jupiter.api.Assertions.assertEquals(2, bank.getAccountCount());
        System.out.println("=== FINAL RESULTS ===");

        System.out.println(
                "Account 1 balance: " + account1.getBalance()
        );

        System.out.println(
                "Account 2 balance: " + account2.getBalance()
        );

        System.out.println(
                "Transaction count: " + bank.getTransactionCount()
        );

        System.out.println(
                "Account count: " + bank.getAccountCount()
        );
    }
    @org.junit.jupiter.api.Test
    void accountOperationsRejectInvalidRequests() {
        Bank bank = new Bank();
        BankAccount account = new BankAccount(1, "Misho", 100);
        org.junit.jupiter.api.Assertions.assertTrue(bank.addAccount(account));
        org.junit.jupiter.api.Assertions.assertFalse(bank.addAccount(new BankAccount(1, "Other", 0)));
        org.junit.jupiter.api.Assertions.assertTrue(bank.deposit(1, 20));
        org.junit.jupiter.api.Assertions.assertTrue(bank.withdraw(1, 10));
        org.junit.jupiter.api.Assertions.assertFalse(bank.withdraw(1, 200));
        org.junit.jupiter.api.Assertions.assertFalse(bank.deposit(1, Double.NaN));
        org.junit.jupiter.api.Assertions.assertFalse(bank.deposit(999, 20));
        org.junit.jupiter.api.Assertions.assertFalse(bank.transfer(1, 1, 5));
        org.junit.jupiter.api.Assertions.assertEquals(110, account.getBalance());
        org.junit.jupiter.api.Assertions.assertEquals(2, bank.getTransactionCount());
    }
}

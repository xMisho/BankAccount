# BankAccount

A concurrent Java banking simulation focused on deposits, withdrawals, transfers, transaction tracking, and safe lock ordering.

Java 17+ · Maven · JUnit 5 · No production dependencies

## Features

- Account registration, lookup, removal, and display.
- Deposits and withdrawals with transaction recording.
- Transfers lock both accounts in account-ID order.
- Per-account reentrant locks protect balance updates and reads.
- Concurrent account storage and transaction history.
- Atomic transaction count and account-specific history display.

## Technical concepts

`ConcurrentHashMap`, `ConcurrentLinkedQueue`, `AtomicInteger`, `ReentrantLock`, nested locking, and consistent lock ordering.

## Architecture

| Class | Responsibility |
| --- | --- |
| `BankAccount` | Account identity, balance, and per-account lock |
| `Transaction` | Immutable transaction metadata |
| `Bank` | Account registry, operations, transfers, and transaction history |

```text
BankAccount/
├── README.md
├── .gitignore
├── pom.xml
└── src/
    ├── main/java/com/misho/bank/
    │   ├── Bank.java
    │   ├── BankAccount.java
    │   └── Transaction.java
    └── test/java/com/misho/bank/
        └── BankAccountTest.java
```

## Build and run

Install JDK 17 or newer and Maven 3.9+. Clone the repository and run its tests:

```bash
git clone https://github.com/xMisho/BankAccount.git
cd BankAccount
mvn test
```

Build and verify the library JAR:

```bash
mvn clean verify
```

The artifact is written to `target/bank-account-1.0.0-SNAPSHOT.jar`. These classes are a library and test suite, so the JAR has no application entry point. Run the JUnit class from an IDE, or run a single suite with:

```bash
mvn -Dtest=BankAccountTest test
```

## Testing

Two JUnit tests cover basic operations and invalid requests, plus concurrent deposits, withdrawals, and transfers.

The concurrent test starts four workers performing 10,000 operations each. It verifies final balances of **110,000 and 90,000**, a conserved combined balance of **200,000**, and **40,000 recorded transactions**. Worker failures are propagated to JUnit.

Surefire writes detailed results to `target/surefire-reports/`. Concurrency tests validate invariants for the exercised workload; they are not a proof for every possible thread schedule.

## Example behavior

Use these classes from the `com.misho.bank` package (and import `java.math.BigDecimal` for the exchange example):

```java
Bank bank = new Bank();
bank.addAccount(new BankAccount(1, "Misho", 1_000));
bank.addAccount(new BankAccount(2, "John", 500));
bank.transfer(1, 2, 100);

System.out.println(bank.getAccount(1).getBalance()); // 900.0
System.out.println(bank.getAccount(2).getBalance()); // 600.0
System.out.println(bank.getTransactionCount());     // 1
```

## Scope and limitations

This is a concurrency exercise, not a production banking service. Amounts use `double`; real financial software needs a carefully specified decimal or integer money model. Constructor validation and direct account deposits are intentionally basic. Prefer bank operations when transaction tracking is required.

Account removal is not coordinated with in-flight operations; keep the account registry stable during concurrent transactions. Transaction-history views and counters are separate observations, not a transactional snapshot. There is no persistence, authentication, network service, or audit durability.

## What this project taught me

Locking an account prevents lost updates, but transfers require two locks. Acquiring both locks in a consistent account-ID order avoids the classic opposite-direction transfer deadlock. Concurrent history storage and atomic counters provide useful records without a global balance lock.

## Project history

Extracted from [Java-Projects](https://github.com/xMisho/Java-Projects), preserving commits that changed this project’s Java sources, including original authors, timestamps, and commit messages. Commit hashes changed because unrelated files and directory prefixes were removed. The Maven migration is a subsequent commit. The original monorepo remains available.

The layout follows [Maven’s standard directory conventions](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html); tests use [JUnit 5](https://docs.junit.org/5.11.4/user-guide/).

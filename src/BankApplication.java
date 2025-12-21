import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Перечисление для типов операций
enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER
}

// Класс Клиент
class Customer {
    private static int nextId = 1;
    private final int id;
    private final String fullName;

    public Customer(String fullName) {
        this.id = nextId++;
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", ФИО: " + fullName;
    }
}

// Класс Транзакция
class Transaction {
    private final TransactionType type;
    private final double amount;
    private final String fromAccountNumber;
    private final String toAccountNumber;
    private final LocalDateTime timestamp;
    private final boolean success;
    private final String message;

    public Transaction(TransactionType type, double amount, String fromAccountNumber,
                       String toAccountNumber, boolean success, String message) {
        this.type = type;
        this.amount = amount;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.timestamp = LocalDateTime.now();
        this.success = success;
        this.message = message;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return String.format("%s | %s | Сумма: %.2f | Счёт отправителя: %s | Счёт получателя: %s | Статус: %s | %s",
                timestamp.format(formatter),
                type.toString(),
                amount,
                fromAccountNumber != null ? fromAccountNumber : "N/A",
                toAccountNumber != null ? toAccountNumber : "N/A",
                success ? "Успешно" : "Ошибка",
                message);
    }

    public TransactionType getType() {
        return type;
    }

    public boolean isSuccess() {
        return success;
    }
}

// Базовый класс Счёт
abstract class Account {
    private static int nextAccountNumber = 100000;
    private final String accountNumber;
    protected double balance;
    private final Customer owner;

    public Account(Customer owner) {
        this.accountNumber = String.valueOf(nextAccountNumber++);
        this.balance = 0.0;
        this.owner = owner;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public boolean transfer(Account to, double amount) {
        if (withdraw(amount)) {
            to.deposit(amount);
            return true;
        }
        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return String.format("Счёт №%s | Владелец: %s | Баланс: %.2f | Тип: %s",
                accountNumber, owner.getFullName(), balance, this.getClass().getSimpleName());
    }

    public abstract String getAccountType();
}

// Дебетовый счёт
class DebitAccount extends Account {
    public DebitAccount(Customer owner) {
        super(owner);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > getBalance()) {
            return false;
        }
        return super.withdraw(amount);
    }

    @Override
    public String getAccountType() {
        return "Дебетовый";
    }
}

// Кредитный счёт
class CreditAccount extends Account {
    private final double creditLimit;

    public CreditAccount(Customer owner, double creditLimit) {
        super(owner);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        double availableBalance = getBalance() + creditLimit;
        if (amount > availableBalance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    @Override
    public String getAccountType() {
        return String.format("Кредитный (лимит: %.2f)", creditLimit);
    }

    public double getCreditLimit() {
        return creditLimit;
    }
}

// Класс Банк
class Bank {
    private final List<Customer> customers;
    private final List<Account> accounts;
    private final List<Transaction> transactions;

    public Bank() {
        customers = new ArrayList<>();
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    // Создание клиента
    public Customer createCustomer(String fullName) {
        Customer customer = new Customer(fullName);
        customers.add(customer);
        return customer;
    }

    // Открытие дебетового счёта
    public Account openDebitAccount(Customer owner) {
        Account account = new DebitAccount(owner);
        accounts.add(account);
        return account;
    }

    // Открытие кредитного счёта
    public Account openCreditAccount(Customer owner, double creditLimit) {
        Account account = new CreditAccount(owner, creditLimit);
        accounts.add(account);
        return account;
    }

    // Поиск счёта по номеру
    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    // Пополнение счёта
    public boolean deposit(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            transactions.add(new Transaction(TransactionType.DEPOSIT, amount,
                    null, accountNumber, false, "Счёт не найден"));
            return false;
        }

        boolean success = account.deposit(amount);
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount,
                null, accountNumber, success, success ? "OK" : "Неверная сумма"));
        return success;
    }

    // Снятие со счёта
    public boolean withdraw(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            transactions.add(new Transaction(TransactionType.WITHDRAW, amount,
                    accountNumber, null, false, "Счёт не найден"));
            return false;
        }

        boolean success = account.withdraw(amount);
        transactions.add(new Transaction(TransactionType.WITHDRAW, amount,
                accountNumber, null, success, success ? "OK" : "Недостаточно средств или неверная сумма"));
        return success;
    }

    // Перевод между счетами
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = findAccount(fromAccountNumber);
        Account toAccount = findAccount(toAccountNumber);

        if (fromAccount == null || toAccount == null) {
            transactions.add(new Transaction(TransactionType.TRANSFER, amount,
                    fromAccountNumber, toAccountNumber, false, "Один из счетов не найден"));
            return false;
        }

        if (fromAccount.withdraw(amount)) {
            toAccount.deposit(amount);
            transactions.add(new Transaction(TransactionType.TRANSFER, amount,
                    fromAccountNumber, toAccountNumber, true, "OK"));
            return true;
        } else {
            transactions.add(new Transaction(TransactionType.TRANSFER, amount,
                    fromAccountNumber, toAccountNumber, false, "Недостаточно средств или неверная сумма"));
            return false;
        }
    }

    // Показать счета клиента
    public void printCustomerAccounts(int customerId) {
        System.out.println("\n=== Счета клиента ID: " + customerId + " ===");
        boolean found = false;

        for (Account account : accounts) {
            if (account.getOwner().getId() == customerId) {
                System.out.println(account);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Счета не найдены или клиент не существует");
        }
    }

    // Показать все транзакции
    public void printTransactions() {
        System.out.println("\n=== Все транзакции ===");
        if (transactions.isEmpty()) {
            System.out.println("Транзакций нет");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    // Отчёт банка
    public void printReport() {
        System.out.println("\n=== Отчёт банка ===");

        // Подсчёт счетов по типам
        int debitCount = 0;
        int creditCount = 0;
        double debitTotal = 0.0;
        double creditTotal = 0.0;

        for (Account account : accounts) {
            if (account instanceof DebitAccount) {
                debitCount++;
                debitTotal += account.getBalance();
            } else if (account instanceof CreditAccount) {
                creditCount++;
                creditTotal += account.getBalance();
            }
        }

        System.out.println("Статистика счетов:");
        System.out.printf("  Дебетовых счетов: %d, суммарный баланс: %.2f%n", debitCount, debitTotal);
        System.out.printf("  Кредитных счетов: %d, суммарный баланс: %.2f%n", creditCount, creditTotal);
        System.out.printf("  Всего счетов: %d, общий баланс: %.2f%n", debitCount + creditCount, debitTotal + creditTotal);

        // Подсчёт операций
        int successCount = 0;
        int failedCount = 0;

        for (Transaction transaction : transactions) {
            if (transaction.isSuccess()) {
                successCount++;
            } else {
                failedCount++;
            }
        }

        System.out.println("\nСтатистика операций:");
        System.out.printf("  Успешных операций: %d%n", successCount);
        System.out.printf("  Неуспешных операций: %d%n", failedCount);
        System.out.printf("  Всего операций: %d%n", successCount + failedCount);

        // Количество клиентов
        System.out.println("\nКлиенты:");
        System.out.printf("  Всего клиентов: %d%n", customers.size());
    }

    // Геттеры для списков (если понадобятся)
    public List<Customer> getCustomers() {
        return customers;
    }
}

// Главный класс приложения
public class BankApplication {
    private static Bank bank = new Bank();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Банковская система ===");

        while (true) {
            printMenu();
            int choice = getIntInput("Выберите пункт меню: ");

            switch (choice) {
                case 1 -> createCustomer();
                case 2 -> openDebitAccount();
                case 3 -> openCreditAccount();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transfer();
                case 7 -> showCustomerAccounts();
                case 8 -> showTransactions();
                case 9 -> showReport();
                case 10 -> {
                    System.out.println("Выход из программы...");
                    return;
                }
                default -> System.out.println("Неверный пункт меню!");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Главное меню ===");
        System.out.println("1. Создать клиента");
        System.out.println("2. Открыть дебетовый счёт");
        System.out.println("3. Открыть кредитный счёт");
        System.out.println("4. Пополнить счёт");
        System.out.println("5. Снять со счёта");
        System.out.println("6. Перевести между счетами");
        System.out.println("7. Показать счета клиента");
        System.out.println("8. Показать транзакции");
        System.out.println("9. Отчёт банка");
        System.out.println("10. Выход");
    }

    private static void createCustomer() {
        System.out.print("Введите ФИО клиента: ");
        String fullName = scanner.nextLine();

        Customer customer = bank.createCustomer(fullName);
        System.out.println("Клиент создан: " + customer);
    }

    private static void openDebitAccount() {
        int customerId = getIntInput("Введите ID клиента: ");
        Customer customer = findCustomerById(customerId);

        if (customer != null) {
            Account account = bank.openDebitAccount(customer);
            System.out.println("Дебетовый счёт открыт: " + account);
        } else {
            System.out.println("Клиент с ID " + customerId + " не найден");
        }
    }

    private static void openCreditAccount() {
        int customerId = getIntInput("Введите ID клиента: ");
        Customer customer = findCustomerById(customerId);

        if (customer != null) {
            double creditLimit = getDoubleInput("Введите кредитный лимит: ");
            Account account = bank.openCreditAccount(customer, creditLimit);
            System.out.println("Кредитный счёт открыт: " + account);
        } else {
            System.out.println("Клиент с ID " + customerId + " не найден");
        }
    }

    private static void deposit() {
        String accountNumber = getStringInput("Введите номер счёта: ");
        double amount = getDoubleInput("Введите сумму для пополнения: ");

        boolean success = bank.deposit(accountNumber, amount);
        System.out.println(success ? "Пополнение успешно!" : "Ошибка при пополнении!");
    }

    private static void withdraw() {
        String accountNumber = getStringInput("Введите номер счёта: ");
        double amount = getDoubleInput("Введите сумму для снятия: ");

        boolean success = bank.withdraw(accountNumber, amount);
        System.out.println(success ? "Снятие успешно!" : "Ошибка при снятии!");
    }

    private static void transfer() {
        String fromAccount = getStringInput("Введите номер счёта отправителя: ");
        String toAccount = getStringInput("Введите номер счёта получателя: ");
        double amount = getDoubleInput("Введите сумму для перевода: ");

        boolean success = bank.transfer(fromAccount, toAccount, amount);
        System.out.println(success ? "Перевод успешен!" : "Ошибка при переводе!");
    }

    private static void showCustomerAccounts() {
        int customerId = getIntInput("Введите ID клиента: ");
        bank.printCustomerAccounts(customerId);
    }

    private static void showTransactions() {
        bank.printTransactions();
    }

    private static void showReport() {
        bank.printReport();
    }

    // Вспомогательные методы
    private static Customer findCustomerById(int id) {
        for (Customer customer : bank.getCustomers()) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    private static int getIntInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите целое число.");
            }
        }
    }

    private static double getDoubleInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите число.");
            }
        }
    }

    private static String getStringInput(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
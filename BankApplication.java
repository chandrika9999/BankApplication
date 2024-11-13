import java.util.ArrayList;
import java.util.Scanner;

public class BankApplication {
    private static final ArrayList<User> users = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Register" +
                    "           \n2. Login" +
                    "           \n3. Exit");
            System.out.print("select from above options ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
                    System.out.println("==================================================");
                    break;
            }
        }
    }

        private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();


        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username already exists. Please try a different username.");
                System.out.println("==================================================");
                return;
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("Registration successful!");
        System.out.println("==================================================");
    }

    private static void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();


        for (User user : users) {
            if (user.getUsername().equals(username) && user.validatePassword(password)) {
                System.out.println("Login successful!");
                System.out.println("==================================================");
                user.showUserMenu();
                return;
            }
        }
        System.out.println("Invalid credentials. Please try again.");
        System.out.println("==================================================");
    }

    static class User {
        private final String username;
        private final String password;
        private final ArrayList<Account> accounts = new ArrayList<>();

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public boolean validatePassword(String password) {
            return this.password.equals(password);
        }

        public void showUserMenu() {
            boolean isLoggedIn = true;

            while (isLoggedIn) {
                System.out.println("\n1. Open Account\n2. View Balance\n3. Deposit\n4. Withdraw\n5. View Transactions\n6. View Statement\n7. Add Monthly Interest\n8. Logout");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        openAccount();
                        break;
                    case 2:
                        viewBalance();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        viewTransactions();
                        break;
                    case 6:
                        viewStatement();
                        break;
                    case 7:
                        addMonthlyInterest();
                        break;
                    case 8:
                        System.out.println("Logged out.");
                        System.out.println("==================================================");
                        isLoggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        System.out.println("==================================================");
                        break;
                }
            }
        }

        private void openAccount() {
            System.out.print("Enter account holder name: ");
            String holderName = scanner.nextLine();
            System.out.print("Enter account type (savings/checking): ");
            String accountType = scanner.nextLine();
            System.out.print("Enter initial deposit amount: ");
            double initialDeposit = scanner.nextDouble();
            scanner.nextLine();

            Account account = new Account(holderName, accountType, initialDeposit);
            accounts.add(account);
            System.out.println("==================================================");
            System.out.println("Account opened successfully! ");
            System.out.println("Account Number: " + account.getAccountNumber());

        }

        private void viewBalance() {
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
                System.out.println("==================================================");
            } else {
                for (Account account : accounts) {
                    System.out.println("Account Number: " + account.getAccountNumber() );
                    System.out.println(" Balance: $" + account.getBalance());
                    System.out.println("==================================================");
                }
            }
        }

        private void deposit() {
            if (accounts.isEmpty()) {
                System.out.println("No accounts available to deposit into.");
                System.out.println("==================================================");
                return;
            }
            System.out.print("Enter account number to deposit into: ");
            int accountNumber = scanner.nextInt();
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); 

            Account account = getAccountByNumber(accountNumber);
            if (account != null) {
                account.deposit(amount);
            } else {
                System.out.println("Account not found.");
                System.out.println("==================================================");
            }
        }

        private void withdraw() {
            if (accounts.isEmpty()) {
                System.out.println("No accounts available to withdraw from.");
                System.out.println("==================================================");
                return;
            }
            System.out.print("Enter account number to withdraw from: ");
            int accountNumber = scanner.nextInt();
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); 

            Account account = getAccountByNumber(accountNumber);
            if (account != null) {
                account.withdraw(amount);
            } else {
                System.out.println("Account not found.");
            }
        }

        private void viewTransactions() {
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
            } else {
                for (Account account : accounts) {
                    System.out.println("Transactions for Account Number: " + account.getAccountNumber());
                    System.out.println("==================================================");
                    account.showTransactions();
                }
            }
        }

        private void viewStatement() {
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
                System.out.println("==================================================");
            } else {
                for (Account account : accounts) {
                    account.showStatement();
                }
            }
        }

        private void addMonthlyInterest() {
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
            } else {
                for (Account account : accounts) {
                    account.addMonthlyInterest();
                }
            }
        }

        private Account getAccountByNumber(int accountNumber) {
            for (Account account : accounts) {
                if (account.getAccountNumber() == accountNumber) {
                    return account;
                }
            }
            return null;
        }
    }

    static class Account {
        private static int accountCounter = 1;
        private final int accountNumber;
        private final String holderName;
        private final String accountType;
        private double balance;
        private double total;
        private final ArrayList<Transaction> transactions = new ArrayList<>();

        public Account(String holderName, String accountType, double initialDeposit) {
            this.accountNumber = accountCounter++;
            this.holderName = holderName;
            this.accountType = accountType;
            this.balance = initialDeposit;
            transactions.add(new Transaction("Deposit", initialDeposit));
            System.out.println("Account created for " + holderName + " with initial balance $" + initialDeposit);
        }

        public int getAccountNumber() {
            return accountNumber;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            balance += amount;
            transactions.add(new Transaction("Deposit", amount));
            System.out.println("Deposited $" + amount);
            System.out.println("Available Balance :" + balance);
        }

        public void withdraw(double amount) {
            if (amount <= balance) {
                balance -= amount;
                transactions.add(new Transaction("Withdraw", amount));
                System.out.println("Withdrew $" + amount);
                System.out.println("Remaining balance: " + balance);
            } else {
                System.out.println("Insufficient balance.");
            }
        }

        public void showTransactions() {
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                for (Transaction transaction : transactions) {
                    System.out.println(transaction);
                }
            }
        }

        public void showStatement() {
            System.out.println("Statement for Account Number: " + accountNumber);
            if (transactions.isEmpty()) {
                System.out.println("No transactions for this account.");
            } else {
                for (Transaction transaction : transactions) {
                    System.out.println(transaction);
                }
            }
        }

        public void addMonthlyInterest() {
            if ("savings".equalsIgnoreCase(accountType)) {

                System.out.println("Enter the number of months: ");
                int month = scanner.nextInt();
                double interest = balance * 0.02 * month;
                balance += interest;

                transactions.add(new Transaction("Interest", interest));
                System.out.println("Interest added: " + interest);

                System.out.println("Amount after adding the interest :  "+(balance + interest) +" for "+month+ "Months");

            }
        }
    }

    static class Transaction {
        private final String type;
        private final double amount;

        public Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Transaction: " + type + " | Amount: " + amount;

        }
    }
}

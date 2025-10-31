import Exceptions.AccountNotFoundException;
import Exceptions.InvalidNameException;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankingSystem {
    private static final Scanner input = new Scanner(System.in);
    private static final Bank bank = new Bank();

    public static void main(String[] args) {
        System.out.println("Welcome to Banking System");
        boolean running = true;
        while (running) {
            try {
                printMainMenu();
                System.out.print("Enter your choice: ");
                int choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                        handleCreateAccount();
                        break;
                    case 2:
                        handleAccountOperations();
                        break;
                    case 3: {
                        running = false;
                        System.out.println("Thank you for using Banking System");
                        break;
                    }
                    default:
                        System.out.println("Wrong choice");


                }

            }
            catch (Exception e){
                System.out.println("Wrong choice");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("Main Menu");
        System.out.println("1. Create an account ");
        System.out.println("2. Perform operations on existing accounts ");
        System.out.println("3. Exit the program ");
    }
    private static void handleCreateAccount() {
        System.out.print("Enter your full name: ");
        String name = input.nextLine();
        try {
            Account acc = bank.createAccount(name);
            System.out.println("Account created successfully: " + acc.getAccountNumber());
            System.out.println(acc);
        } catch (InvalidNameException ine) {
            System.out.println("Failed to create account: " + ine.getMessage());
        } catch (javax.naming.InvalidNameException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleAccountOperations() {
        System.out.print("Enter account number: ");
        String accNo = input.nextLine().trim();
        try {
            Account acc = bank.getAccount(accNo);
            boolean back = false;
            while (!back) {
                printAccountMenu(accNo);
                int ch = readInt();
                switch (ch) {
                    case 1 -> handleDeposit(accNo);
                    case 2 -> handleWithdraw(accNo);
                    case 3 -> handleTransfer(accNo);
                    case 4 -> showBalance(accNo);
                    case 5 -> back = true;
//                    case 6 -> runConcurrentSimulation(accNo);
                    default -> System.out.println("Invalid choice");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printAccountMenu(String accNo) {
        System.out.println("\n--- Operations for " + accNo + " ---");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. Show balance");
        System.out.println("5. Return to main menu");
        System.out.println("6. (Demo) Run concurrent transactions on this account");
        System.out.print("Enter choice: ");
    }

    private static void handleDeposit(String accNo) {
        try {
            System.out.print("Enter amount to deposit: ");
            double amt = readDouble();
            bank.deposit(accNo, amt);
            System.out.println("Deposit successful. New balance: " + bank.getAccount(accNo).getBalance());
        } catch (Exception e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    private static void handleWithdraw(String accNo) {
        try {
            System.out.print("Enter amount to withdraw: ");
            double amt = readDouble();
            bank.withdraw(accNo, amt);
            System.out.println("Withdrawal successful. New balance: " + bank.getAccount(accNo).getBalance());
        } catch (Exception e) {
            System.out.println("Withdraw failed: " + e.getMessage());
        }
    }


    private static void handleTransfer(String fromAcc) {
        try {
            System.out.print("Enter destination account number: ");
            String toAcc = input.nextLine().trim();
            System.out.print("Enter amount to transfer: ");
            double amt = readDouble();
            bank.transfer(fromAcc, toAcc, amt);
            System.out.println("Transfer successful.");
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    private static void showBalance(String accNo) {
        try {
            Account a = bank.getAccount(accNo);
            System.out.println(a);
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


//    private static void runConcurrentSimulation(String accNo) {
//        System.out.println("Starting concurrent demo: 4 threads (2 deposits, 2 withdraws)");
//        ExecutorService ex = Executors.newFixedThreadPool(4);
//        ex.execute(new TransactionTask(bank, accNo, TransactionTask.Type.DEPOSIT, 500));
//        ex.execute(new TransactionTask(bank, accNo, TransactionTask.Type.WITHDRAW, 200));
//        ex.execute(new TransactionTask(bank, accNo, TransactionTask.Type.DEPOSIT, 300));
//        ex.execute(new TransactionTask(bank, accNo, TransactionTask.Type.WITHDRAW, 400));
//        ex.shutdown();
//        try { ex.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
//        try { System.out.println("Final balance: " + bank.getAccount(accNo).getBalance()); } catch (AccountNotFoundException ignored) {}
//    }


    private static int readInt() {
        while (true) {
            try {
                String line = input.nextLine();
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException nfe) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }


    private static double readDouble() {
        while (true) {
            try {
                String line = input.nextLine();
                return Double.parseDouble(line.trim());
            } catch (NumberFormatException nfe) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

}


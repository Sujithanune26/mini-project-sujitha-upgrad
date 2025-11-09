import Exceptions.InsufficientBalanceException;
import Exceptions.InvalidAmountException;

import Exceptions.InvalidNameException;
import java.util.Random;

public  abstract class Account {
    private static final Random RAND=new Random();
    private final String accountNumber;
    private final String name;
    private double balance;

    public Account(String name) throws InvalidNameException {
        if (name==null || name.trim().isEmpty()) throw new InvalidNameException("Name cannot be empty");
        if (!name.matches("[a-zA-Z ]+")) {
            throw new InvalidNameException("Name must contain only alphabets (A–Z or a–z)");
        }
        if(name.length()<2){
            throw new InvalidNameException("Name must have at least 2 characters");
        }
        this.name = name.trim();
        this.accountNumber=generateAccountNumber(this.name);
        this.balance=0.0;
    }

    private String generateAccountNumber(String name) {
        String initials="xx";
        String[] parts=name.trim().split("\\s+");
        if (parts.length==1) initials =parts[0].substring(0,2).toUpperCase();
        else initials=(""+parts[0].charAt(0)+parts[parts.length-1].charAt(0)).toUpperCase();
        int num=1000+RAND.nextInt(8999);
        return initials+num;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getName() {
        return name;
    }
    public synchronized void deposit(double amount) throws InvalidAmountException {
        if (amount <=0) throw new  InvalidAmountException("Deposit amount must be positive");
        balance+=amount;
    }
    public synchronized void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        try {
            if (amount <= 0)
                throw new InvalidAmountException("Withdraw amount must be positive");

            if (amount > balance)
                throw new InsufficientBalanceException("Insufficient balance. Current: " + balance);

            // Perform the withdrawal
            balance -= amount;

        } catch (InvalidAmountException | InsufficientBalanceException e) {
            // Restore balance (though in this logic it's not yet modified)
            System.err.println("Withdrawal failed: " + e.getMessage());
        } finally {
            // Log or audit always happens, even if exception occurs
            System.out.println("Final balance check: " + balance);
        }
    }
    public synchronized double getBalance() {
        return balance;
    }
    @Override
    public String toString() {
        return String.format("Account[%s] Name:%s Balance:%.2f",accountNumber,name,balance);

    }

}

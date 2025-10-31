import Exceptions.InsufficientBalanceException;
import Exceptions.InvalidAmountException;

import javax.naming.InvalidNameException;
import java.util.Random;

public  abstract class Account {
    private static final Random RAND=new Random();
    private final String accountNumber;
    private final String name;
    private double balance;

    public Account(String name) throws InvalidNameException {
        if (name==null || name.trim().isEmpty()) throw new InvalidNameException("Name cannot be empty");
        this.name = name.trim();
        this.accountNumber=generateAccountNumber(this.name);
        this.balance=0.0;
    }

    private String generateAccountNumber(String name) {
        String initials="xx";
        String[] parts=name.trim().split("\\s+");
        if (parts.length==1) initials =parts[0].substring(0,Math.min(2,parts[0].length())).toUpperCase();
        else initials=(""+parts[0].charAt(0)+parts[parts.length-1].charAt(0)).toUpperCase();
        int num=1000+RAND.nextInt(9000);
        return initials+num;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getName() {
        return name;
    }
    public synchronized void deposit(double amount) throws InvalidAmountException {
        if (amount <=0) throw new  InvalidAmountException("Deposite amount must be positive");
        balance+=amount;
    }
    public synchronized void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount<=0) throw new InvalidAmountException("with draw amount must be positive ");
        if (amount>balance) throw new InsufficientBalanceException("insufficient balance.current:"+balance);
        balance-=amount;
    }
    public synchronized double getBalance() {
        return balance;
    }
    @Override
    public String toString() {
        return String.format("Account[%s] Name:%s Balance:%.2f",accountNumber,name,balance);

    }

}

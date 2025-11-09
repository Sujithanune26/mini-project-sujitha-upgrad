import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import Exceptions.AccountNotFoundException;
import Exceptions.InsufficientBalanceException;
import Exceptions.InvalidAmountException;
import Exceptions.InvalidNameException;

public class Bank {
    private final Map<String, Account> accounts = new ConcurrentHashMap<String, Account>();

    public Account createAccount(String name) throws InvalidNameException {
        Account acc = new SimpleAccount(name);
        accounts.put(acc.getAccountNumber(), acc);
        return acc;
    }

    public Account getAccount(String accNo) throws AccountNotFoundException {
        Account a = accounts.get(accNo);
        if (a == null) throw new AccountNotFoundException("Account not found:" + accNo);
        return a;
    }

    public List<Account> findAccountsByName(String namePart) {
        return accounts.values().stream()
                .filter(a -> a.getName().toLowerCase().contains(namePart.toLowerCase())).collect(Collectors.toList());

    }

    public void deposit(String accNo, double amount) throws AccountNotFoundException, InvalidAmountException, InvalidNameException {
        Account a = getAccount(accNo);
        a.deposit(amount);
    }

    public void withdraw(String accNo, double amount) throws AccountNotFoundException, InvalidAmountException, InvalidNameException, InsufficientBalanceException {
        Account a = getAccount(accNo);
        a.withdraw(amount);

    }

    public void transfer(String fromAcc, String toAcc, double amount) throws AccountNotFoundException, InvalidAmountException, InvalidNameException, InsufficientBalanceException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
        Account a1 = getAccount(fromAcc);
        Account a2 = getAccount(toAcc);
        Account first = a1.getAccountNumber().compareTo(a2.getAccountNumber()) == 0 ? a1 : a2;
        Account second = first == a1 ? a2 : a1;
        synchronized (first) {
            synchronized (second) {
                a1.withdraw(amount);
                a2.deposit(amount);
            }
        }
    }

    public Collection<Account> listAllAccounts() {
        return accounts.values();
    }
}

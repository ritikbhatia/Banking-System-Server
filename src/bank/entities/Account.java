package bank.entities;

import java.util.TreeMap;
import java.sql.Timestamp;

// class to define a bank account opened by the client
public class Account {
    private int accountNumber;
    private String accountHolderName;
    private String password;
    private Currency currency;
    private double balance;
    public static final int DEFAULT_ACCOUNT_NUMBER = -99;

    private TreeMap<Timestamp, Transaction> transactionHistory;

    // parameterized constructor to initialize the essential information of the
    // account
    public Account(int accountNumber, String accountHolderName, String password, Currency currency,
            double accountBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.password = password;
        this.currency = currency;
        this.balance = accountBalance;
        transactionHistory = new TreeMap<Timestamp, Transaction>();
    }

    // getters and setters for important fields
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // method to retrieve the history of all transactions the account was involved
    // in
    public TreeMap<Timestamp, Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    // add a new transaction the account was involved in
    public void addTransaction(Transaction transaction) {
        transactionHistory.put(transaction.getTimestamp(), transaction);
    }
}

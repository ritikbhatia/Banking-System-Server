package bank.entities;

import java.util.HashMap;
import java.sql.Timestamp;

public class Account{
    private int accountNumber;
    private String accountHolderName;
    private String password;
    private Currency currency; 
    private double balance;

    private HashMap<Timestamp, Transaction> transactionHistory;

    public Account(){}

    public Account(int accountNumber, String accountHolderName, String password, Currency currency, double accountBalance){
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.password = password;
        this.currency = currency;
        this.balance = accountBalance;
    }

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

    public HashMap<Timestamp, Transaction> getTranactionHistory(){
        return transactionHistory;
    }

    public void addTransaction(Transaction transaction){
        transactionHistory.put(transaction.getTimestamp(), transaction);
    }
}




package bank.entities;

import java.sql.Timestamp;

// class to define a transaction in the bank
public class Transaction {

    private final Timestamp timestamp;
    private OpType operation;
    private Currency currency;
    private double amount;
    private double balance;
    private String information;

    // parameterized constructor to initialize a transaction with important
    // information
    public Transaction(OpType operation, Currency currency, double amount, double balance, String information) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.operation = operation;
        this.currency = currency;
        this.amount = amount;
        this.balance = balance;
        this.information = information;
    }

    // getters and setters below for essential components of a transaction
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public OpType getOperation() {
        return operation;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getInformation() {
        return information;
    }

    public void setOperation(OpType operation) {
        this.operation = operation;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}

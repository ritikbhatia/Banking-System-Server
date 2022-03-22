package bank.entities;

import java.sql.Timestamp;

public class Transaction{

    private final Timestamp timestamp;
    private OpType operation;
    private Currency currency;
    private double amount;
    private String information;

    public Transaction(OpType operation, Currency currency, double amount,  String information){
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.operation = operation;
        this.currency = currency;
        this.amount = amount;
        this.information = information;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setOperation(OpType operation) {
        this.operation = operation;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public OpType getOperation() {
        return operation;
    }
}


package entity;

import java.sql.Timestamp;
import entity.Currency;

public class Transaction{

    private final Timestamp timestamp;
    private String Operation;
    private double amount;
    private Currency currency;
    private String information;

    public Transaction(String operation, double amount, Currency currency, String information){
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.Operation = operation;
        this.amount = amount;
        this.currency = currency;
        this.information = information;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setOperation(String operation) {
        Operation = operation;
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

    public String getOperation() {
        return Operation;
    }
}


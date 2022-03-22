package bank.entities;

import java.util.ArrayList;
import java.util.HashMap;

import bank.services.Service;

public class Bank{
    
    private HashMap<Integer, Account> accounts;
    private ArrayList<Service> services;

    public Bank(){
        accounts = new HashMap<Integer, Account>();
        services = new ArrayList<Service>();
    }

    public void addService(Service service){
        services.add(service);
        service.setBank(this);
    }

    public void addAccount(Account account){
        accounts.put(account.getAccountNumber(), account);
    }

    public void removeAccount(Account account){
        accounts.remove(account.getAccountNumber());
    }

    public Account getAccount(int accountNumber){
        return accounts.get(accountNumber);
    }

    public boolean checkAccountNumberExists(int accountNumber){
        return accounts.containsKey(accountNumber);
    }

}
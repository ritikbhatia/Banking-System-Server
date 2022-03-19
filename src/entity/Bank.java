package entity;

import java.util.ArrayList;
import java.util.HashMap;
import service.Service;

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

    public void getAccount(int accountNumber){
        accounts.get(accountNumber);
    }

}
package service;

import entity.Bank;

public class Service{
    protected Bank bank;

    public Service(){} 

    public Service(Bank bank){
        this.bank = bank;
    }

    public Bank getBank(){
        return bank;
    }

    public void setBank(Bank bank){
        this.bank = bank;
    }

}
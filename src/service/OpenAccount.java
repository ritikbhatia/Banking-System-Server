package service;

import java.util.Random;
import entity.Account;
import entity.Currency;

public class OpenAccount extends Service{
    /*
    A service that allows a user to open a new account by specifying:
        - name
        - password 
        - currency type 
        - initial account balance. 
    returns the account number as the result.
    */

    public int openAccount(String name, String password, Currency currency, float balance){
        Random rand = new Random();
        int accountNumber = rand.nextInt(10000);

        Account account = new Account(accountNumber, name, password, currency, balance);
        bank.addAccount(account);
        
        return account.getAccountNumber();
    }
}
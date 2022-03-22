package service;

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

    private int cur_account_number = 0;


    private int getAccountNumber(){
        
        do{
            cur_account_number++;

            if (cur_account_number == Integer.MAX_VALUE){
                cur_account_number = 1;
            }
        }while (bank.checkAccountNumberExists(cur_account_number));
        
        return cur_account_number;
    }

    public int openAccount(String name, String password, Currency currency, float balance){

        int accountNumber = getAccountNumber();

        Account account = new Account(accountNumber, name, password, currency, balance);
        bank.addAccount(account);
        
        return account.getAccountNumber();
    }
}
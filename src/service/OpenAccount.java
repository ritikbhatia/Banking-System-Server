package service;

import entity.Account;
import entity.Currency;
import utils.Response;
import utils.Response.Status;

public class OpenAccount extends Service{
    /*
    A service that allows a user to open a new account by specifying:
        - name
        - password 
        - currency type 
        - initial account balance. 
    returns response wih the account number.
    */

    private int cur_account_number = 0;

    public OpenAccount() {
        super();
    }

    private int getAccountNumber(){
        
        do{
            cur_account_number++;

            if (cur_account_number == Integer.MAX_VALUE){
                cur_account_number = 1;
            }
        }while (bank.checkAccountNumberExists(cur_account_number));
        
        return cur_account_number;
    }

    public Response openAccount(String name, String password, Currency currency, float balance){

        int accountNumber = getAccountNumber();

        Account account = new Account(accountNumber, name, password, currency, balance);
        bank.addAccount(account);
    
        String mssg = "Account opened successfully with Account number " + accountNumber + " ."; 
        return new Response(Status.SUCCESS, mssg);
    }
}
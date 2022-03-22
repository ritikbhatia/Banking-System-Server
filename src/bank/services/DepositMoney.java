package bank.services;

import bank.entities.Account;
import bank.entities.Currency;
import system.message.Response;
import system.message.Response.Status;

public class DepositMoney extends Service{

    public Response depositMoney(int accountNumber, String accountHolderName, String password, Currency currency, double amount){
        
        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);
       
        if(checkAccountResponse.getStatus() == Status.FAILURE){
           return checkAccountResponse;
        }

        Account bankAccount = bank.getAccount(accountNumber);
        
        if(bankAccount.getCurrency() != currency){
            String mssg = "Currency does not match.";
            return new Response(Status.FAILURE, mssg);
        }
        
        else{
            bankAccount.setBalance(bankAccount.getBalance() + amount);
            String mssg = currency.toString() + " " + String.valueOf(amount) + " deposited into account successfully.";
            return new Response(Status.SUCCESS, mssg);
        }
    }
}

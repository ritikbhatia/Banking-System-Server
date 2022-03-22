package bank.services;

import bank.entities.Account;
import bank.entities.Currency;
import system.message.Response;
import system.message.Response.Status;

public class WithdrawMoney extends Service {

    public Response withdrawMoney(int accountNumber, String accountHolderName, String password, Currency currency, double amount){
        
        Response checkResponse = checkAccountDetails(accountNumber, accountHolderName, password);
       
        if(checkResponse.getStatus() == Status.FAILURE){
           return checkResponse;
        }

        Account bankAccount = bank.getAccount(accountNumber);
        
        if(bankAccount.getCurrency() != currency){
            String mssg = "Currency does not match.";
            return new Response(Status.FAILURE, mssg);
        }
        
        else if(bankAccount.getBalance() < amount){
            String mssg = "Insufficient balance.";
            return new Response(Status.FAILURE, mssg);
        }
        
        else{
            bankAccount.setBalance(bankAccount.getBalance() - amount);
            String mssg = currency.toString() + " " + String.valueOf(amount) + " withdrawn from account successfully.";
            return new Response(Status.SUCCESS, mssg);
        }
    }
    
}

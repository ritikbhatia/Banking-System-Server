package bank.services;

import bank.entities.*;
import system.message.Response;
import system.message.Response.Status;

public class WithdrawMoney extends Service {

    public Response withdrawMoney(int accountNumber, String accountHolderName, String password, int currencyCode, double amount){
        
        Response checkResponse = checkAccountDetails(accountNumber, accountHolderName, password);
        Currency currency = Currency.fromId(currencyCode);

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

            Transaction transaction = new Transaction(OpType.WITHDRAW_MONEY, currency, amount, "Total Balance: " + bankAccount.getBalance());
            bankAccount.addTransaction(transaction);
            
            String mssg = currency.toString() + " " + String.valueOf(amount) + " withdrawn from account successfully.";
            return new Response(Status.SUCCESS, mssg);
        }
    }
    
}

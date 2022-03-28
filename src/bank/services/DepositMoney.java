package bank.services;

import bank.entities.*;
import system.message.Response;
import system.message.Response.Status;

public class DepositMoney extends Service{

    public Response depositMoney(int accountNumber, String accountHolderName, String password, int currencyCode, double amount){
        
        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);
        Currency currency = Currency.fromId(currencyCode);

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

            Transaction transaction = new Transaction(OpType.DEPOSIT_MONEY, currency, amount, "Total Balance: " + bankAccount.getBalance());
            bankAccount.addTransaction(transaction);

            String mssg = currency.toString() + " " + String.valueOf(amount) + " deposited into account successfully.";
            return new Response(Status.SUCCESS, mssg);
        }
    }
}
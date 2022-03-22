package service;

import entity.Account;
import entity.Currency;
import utils.Response;
import utils.Response.Status;

public class TransferMoney extends Service{

    public TransferMoney() {
        super();
    }

    public Response transferMoney(int accountNumber, int payeeAccountNumber, String accountHolderName, String password, Currency currency, double amount){

        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);
       
        if(checkAccountResponse.getStatus() == Status.FAILURE){
           return checkAccountResponse;
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

        else if(!bank.checkAccountNumberExists(payeeAccountNumber)){
            String mssg = "Payee account number " + payeeAccountNumber + " does not exist.";
            return new Response(Status.FAILURE, mssg);
        }
        
        else{
            bankAccount.setBalance(bankAccount.getBalance() - amount);
            Account payeeAccount = bank.getAccount(payeeAccountNumber);
            payeeAccount.setBalance(payeeAccount.getBalance() + amount);

            String mssg = currency.toString() + " " + String.valueOf(amount) + " transferred from account " + accountNumber + " to account " + payeeAccountNumber + " successfully.";
            return new Response(Status.SUCCESS, mssg);
        }
    }
}
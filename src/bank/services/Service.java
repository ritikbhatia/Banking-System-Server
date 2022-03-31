package bank.services;

import bank.entities.*;
import system.message.Response;
import system.message.Response.Status;
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

    // Checks whether account details are correct

    public Response checkAccountDetails(int accountNumber, String accountHolderName, String password){
        
        if(!bank.checkAccountNumberExists(accountNumber)){
            String mssg = "Account number " + accountNumber + " does not exist.";
            return new Response(Status.FAILURE, mssg);
        } 

        Account bankAccount = bank.getAccount(accountNumber);
        
        if (!bankAccount.getAccountHolderName().equals(accountHolderName)){
            String mssg = "Account holder name does not match.";
            return new Response(Status.FAILURE, mssg);
        } 

        else if (!bankAccount.getPassword().equals(password)){
            String mssg = "Password does not match.";
            return new Response(Status.FAILURE, mssg);
        } 

        else return new Response(Status.SUCCESS, "Account details are coorect.");
    }

    protected void updateTransactionHistroy(Account account, Transaction transaction){
        account.addTransaction(transaction);
    }
}
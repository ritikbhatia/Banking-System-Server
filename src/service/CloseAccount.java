package service;

import entity.Account;
import utils.Response;
import utils.Response.Status;

public class CloseAccount extends Service {

    public CloseAccount() {
        super();
    }

    public Response closeAccount(int accountNumber, String accountHolderName, String password) {

        if(!bank.checkAccountNumberExists(accountNumber)){
            String mssg = "Account number " + accountNumber + " does not exist.";
            return new Response(Status.FAILURE, mssg);
        } 

        Account bankAccount = bank.getAccount(accountNumber);
        
        if (bankAccount.getAccountHolderName() != accountHolderName){
            String mssg = "Account holder name does not match.";
            return new Response(Status.FAILURE, mssg);
        } 

        else if (bankAccount.getPassword() != password){
            String mssg = "Password does not match.";
            return new Response(Status.FAILURE, mssg);
        } 

        else {
            bank.removeAccount(bankAccount);
            String mssg = "Account number " + accountNumber + " closed successfully.";
            return new Response(Status.SUCCESS, mssg);
        }
    }
}
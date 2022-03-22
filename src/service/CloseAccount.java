package service;

import entity.Account;
import utils.Response;
import utils.Response.Status;

public class CloseAccount extends Service {

    public CloseAccount() {
        super();
    }

    public Response closeAccount(int accountNumber, String accountHolderName, String password) {

        Response response = checkAccountDetails(accountNumber, accountHolderName, password);
       
        if(response.getStatus() == Status.FAILURE){
           return response;
        }

        Account bankAccount = bank.getAccount(accountNumber);
       
        bank.removeAccount(bankAccount);
        String mssg = "Account number " + accountNumber + " closed successfully.";
        return new Response(Status.SUCCESS, mssg);
    
    }
}
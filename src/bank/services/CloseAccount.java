package bank.services;


import bank.entities.Account;
import system.message.Response;
import system.message.Response.Status;

public class CloseAccount extends Service {

    public CloseAccount() {
        super();
    }

    public Response closeAccount(int accountNumber, String accountHolderName, String password) {

        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);
       
        if(checkAccountResponse.getStatus() == Status.FAILURE){
           return checkAccountResponse;
        }

        Account bankAccount = bank.getAccount(accountNumber);
       
        bank.removeAccount(bankAccount);
        String mssg = "Account number " + accountNumber + " closed successfully.";
        return new Response(Status.SUCCESS, mssg);
    
    }
}
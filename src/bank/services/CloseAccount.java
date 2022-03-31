package bank.services;

import bank.entities.Account;
import system.message.Response;
import system.message.Response.Status;

public class CloseAccount extends Service {

    public CloseAccount() {
        super();
    }

    // Closes account with given id, account name, and password
    // Returns response with closed message if successful

    public Response closeAccount(String accountHolderName, String password, int accountNumber) {

        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);

        if (checkAccountResponse.getStatus() == Status.FAILURE) {
            return checkAccountResponse;
        }

        Account bankAccount = bank.getAccount(accountNumber);

        bank.removeAccount(bankAccount);
        String mssg = "Account number " + accountNumber + " closed successfully.";
        return new Response(Status.SUCCESS, mssg);

    }
}
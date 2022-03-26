package bank.services;

import system.message.Response;
import system.message.Response.Status;
import system.Subscriber;
// import java.net.InetAddress;

public class MonitorUpdates extends Service {

    public Response monitorUpdates(int accountNumber, String accountHolderName, String password) {
        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);
       
        if(checkAccountResponse.getStatus() == Status.FAILURE){
           return checkAccountResponse;
        }

        String mssg = "Monitoring started for client " + accountHolderName;
        return new Response(Status.SUCCESS, mssg);
    }

}

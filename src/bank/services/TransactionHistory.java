package bank.services;

import system.message.Response;
import system.message.Response.Status;

public class TransactionHistory extends Service {

    public Response viewHistory() {
        return new Response(Status.SUCCESS, "Transaction history");
    }

}

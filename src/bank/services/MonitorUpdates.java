package bank.services;

import system.message.Response;
import system.message.Response.Status;

public class MonitorUpdates extends Service {

    public Response monitorUpdates() {
        
        return new Response(Status.SUCCESS, "Monitoring updates");
    }

}

package bank.services;

import system.message.Response;
import system.message.Response.Status;

public class MonitorUpdates extends Service {

    public Response monitorUpdates(int monitoringInterval) {
        String mssg = "Starting monitoring for client with monitoring interval = "
                + Integer.valueOf(monitoringInterval);
        return new Response(Status.SUCCESS, mssg);
    }

}

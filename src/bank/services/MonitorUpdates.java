package bank.services;

import system.message.Response;
import system.message.Response.Status;
import system.Subscriber;
// import java.net.InetAddress;

public class MonitorUpdates extends Service {

    //TODO: Get the arguments for creating a subscriber 
    public Response monitorUpdates(Subscriber subscriber) {
        // Subscriber subscriber = new Subscriber(ip, port, monitorRequestId, monitoringInterval);
        bank.addSubscriber(subscriber);
        String mssg = "Monitoring started for client IP " + subscriber.getMonitoringRequestId() + " until " + (subscriber.getRegistrationTime() + subscriber.getMonitoringInterval() * 1e9);
        return new Response(Status.SUCCESS, mssg);
    }

}

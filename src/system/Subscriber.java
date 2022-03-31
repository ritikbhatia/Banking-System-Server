package system;

import java.net.InetAddress;

// class for defining a Subscriber who wants to monitor updates
public class Subscriber {
    private InetAddress ip;
    private int port;
    private long registrationTime;
    private int monitorRequestId;
    private int monitoringInterval;

    // parameterized constructor to initialize a subscriber
    public Subscriber(InetAddress ip, int port, int monitorRequestId, int monitoringInterval) {
        this.ip = ip;
        this.port = port;
        this.monitorRequestId = monitorRequestId;
        this.monitoringInterval = monitoringInterval;
        this.registrationTime = System.nanoTime();
    }

    // method to check validity of the monitoring interval since the request was
    // made
    public boolean withinMonitoringInterval() {
        return (System.nanoTime() - registrationTime) / 1e9 <= monitoringInterval;
    }

    // getters and setters for class members
    public InetAddress getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getMonitoringRequestId() {
        return monitorRequestId;
    }

    public long getRegistrationTime() {
        return registrationTime;
    }

    public int getMonitoringInterval() {
        return monitoringInterval;
    }
}

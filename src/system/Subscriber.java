package system;

import java.net.InetAddress;

public class Subscriber {
    private InetAddress ip;
    private int port;
    private long registrationTime;
    private int monitorRequestId;
    private int monitoringInterval;

    public Subscriber(InetAddress ip, int port, int monitorRequestId, int monitoringInterval) {
        this.ip = ip;
        this.port = port;
        this.monitorRequestId = monitorRequestId;
        this.monitoringInterval = monitoringInterval;
        this.registrationTime = System.nanoTime();
    }

    public boolean checkMonitoringValidity() {
        return (System.nanoTime() - registrationTime) / 1e9 <= monitoringInterval;
    }

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

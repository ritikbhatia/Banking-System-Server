package entity;

import java.net.InetAddress;

public class Subscriber {
    private InetAddress ip;
    private int port;

    public Subscriber() {
    }

    public InetAddress getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}

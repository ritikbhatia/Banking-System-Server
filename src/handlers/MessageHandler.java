package handlers;

import entity.message.*;

public class MessageHandler {

    public static Request unmarshal(byte[] packetData) {
        return new Request();
    }

    public static byte[] marshal(Response resp) {
        return new byte[1024];
    }

}
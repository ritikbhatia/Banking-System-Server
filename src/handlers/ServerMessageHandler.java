package handlers;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import system.message.Response.Status;

import system.message.*;

public class ServerMessageHandler {

    // making class member as Enum.values() is expensive
    private static Status[] statusValues = Status.values();

    public static Response unmarshal(byte[] packetData) {
        int index = 0;
        ByteBuffer extractor = ByteBuffer.wrap(packetData);

        // retrive type of status (FAILURE / SUCCESS)
        int statusType = extractor.getInt(index);
        index += Integer.BYTES;

        // retrieve length of the message string
        int messageLen = extractor.getInt(index);
        index += Integer.BYTES;

        // retrieve the message string
        String message = new String(Arrays.copyOfRange(packetData, index, index + messageLen), StandardCharsets.UTF_16);

        return new Response(statusValues[statusType], message);
    }

    // TODO: implement marshalling
    public static byte[] marshal(Response resp) {
        return new byte[1024];
    }

    /**
     * 
     * do we also need separate marshalling for message from client and
     * unmarshalling of message on client side?
     */
}
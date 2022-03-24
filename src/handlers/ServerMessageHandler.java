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

    public static byte[] marshal(Response resp) {
        int statusType = resp.getStatus().ordinal();
        String messageString = resp.getMessage();
        int messageLen = messageString.length();

        int contentLen = Integer.BYTES + messageLen;
        ByteBuffer marshalledMesage = ByteBuffer.allocate(contentLen);

        marshalledMesage
                .putInt(statusType)
                .putInt(messageLen)
                .put(messageString.getBytes());

        return marshalledMesage.array();
    }

    /**
     * 
     * do we also need separate marshalling for message from client and
     * unmarshalling of message on client side?
     */
}
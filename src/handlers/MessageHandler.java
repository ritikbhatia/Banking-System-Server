package handlers;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import system.message.Response.Status;

import system.message.*;

public class MessageHandler {

    public static final byte UPCOMING_STRING = 0x01;
    public static final byte UPCOMING_INT = 0x02;
    public static final byte UPCOMING_FLOAT = 0x03;

    // making class member as Enum.values() is expensive
    private static Status[] statusValues = Status.values();

    public static Response unmarshalServerResponse(byte[] packetData) {
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

    public static byte[] marshalServerResponse(Response resp) {
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

    public static Request unmarshalClientRequest(byte[] packetData) {
        int index = 0;
        ByteBuffer extractor = ByteBuffer.wrap(packetData);

        // retrive request id
        int requestId = extractor.getInt(index);
        index += Integer.BYTES;

        // retrieve opType
        int opType = extractor.getInt(index);
        index += Integer.BYTES;

        // retrieve number of arguments
        int numArguments = extractor.getInt(index);
        index += Integer.BYTES;

        Object[] arguments = new Object[numArguments];
        for (int i = 0; i < numArguments; i++) {
            byte upcomingType = extractor.get(index);
            index += Byte.BYTES;
            if (upcomingType == UPCOMING_STRING) {
                int stringLength = extractor.getInt(index);
                index += Integer.BYTES;
                arguments[i] = new String(Arrays.copyOfRange(packetData, index, index + stringLength),
                        StandardCharsets.UTF_16);
                index += stringLength;
            } else if (upcomingType == UPCOMING_INT) {
                arguments[i] = extractor.getInt(index);
                index += Integer.BYTES;
            } else if (upcomingType == UPCOMING_FLOAT) {
                arguments[i] = extractor.getFloat(index);
                index += Float.BYTES;
            }
        }

        return new Request(requestId, opType, arguments);
    }

    // TODO: implement marshalling
    public static byte[] marshalClientRequest(Request resp) {
        return new byte[1024];
    }

}
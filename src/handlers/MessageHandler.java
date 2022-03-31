package handlers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import bank.entities.Currency;

import java.nio.charset.StandardCharsets;
import system.message.Response.Status;

import system.message.*;

// class for marshalling and unmarshalling messages
public class MessageHandler {

    // constants to indicate type of upcoming data in the marshalled message
    public static final byte UPCOMING_STRING = 0x01;
    public static final byte UPCOMING_INT = 0x02;
    public static final byte UPCOMING_DOUBLE = 0x03;

    // making class member as Enum.values() is expensive
    private static Status[] statusValues = Status.values();

    // method to marshal request message from client
    public static byte[] marshalClientRequest(Request req) {
        int requestId = req.getId();
        int opType = req.getType();

        // the first entry to specify the number of arguments
        int argumentContentLength = Integer.BYTES;
        ArrayList<byte[]> argumentContent = new ArrayList<>();

        // in the below marshalling, currency is handled as an integer, specifying the
        // currency type
        // required processing will need to be done at the server's end when serving the
        // request
        for (Object obj : req.getArguments()) {
            if (obj instanceof String) {

                // for marshalling string argument, also mention the length of the string bytes
                byte[] strArg = ((String) obj).getBytes(StandardCharsets.UTF_16);
                argumentContent.add(ByteBuffer.allocate(Byte.BYTES + Integer.BYTES + strArg.length)
                        .put(UPCOMING_STRING)
                        .putInt(strArg.length)
                        .put(strArg)
                        .array());
                argumentContentLength += Byte.BYTES + Double.BYTES + strArg.length;
            } else if (obj instanceof Integer || obj instanceof Currency) {
                argumentContent.add(ByteBuffer.allocate(Byte.BYTES + Integer.BYTES)
                        .put(UPCOMING_INT)
                        .putInt((int) obj)
                        .array());
                argumentContentLength += Byte.BYTES + Integer.BYTES;
            } else if (obj instanceof Double) {
                argumentContent.add(ByteBuffer.allocate(Byte.BYTES + Double.BYTES)
                        .put(UPCOMING_DOUBLE)
                        .putDouble((double) obj)
                        .array());
                argumentContentLength += Byte.BYTES + Double.BYTES;
            }
        }

        // fill the buffer for the marshalled message with the required data
        ByteBuffer marshalledClientRequest = ByteBuffer.allocate(2 * Integer.BYTES + argumentContentLength);
        marshalledClientRequest
                .putInt(requestId)
                .putInt(opType)
                .putInt(argumentContent.size());

        for (byte[] bytes : argumentContent)
            marshalledClientRequest.put(bytes);

        // return the marshalled message
        return marshalledClientRequest.array();
    }

    // method to unmarshal client request
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

        // retrieve the arguments for processing the service requested
        Object[] arguments = new Object[numArguments];
        for (int i = 0; i < numArguments; i++) {

            // determine the type of the upcoming argument
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
            } else if (upcomingType == UPCOMING_DOUBLE) {
                arguments[i] = extractor.getDouble(index);
                index += Double.BYTES;
            }
        }

        // return unmarshalled message of type Request
        return new Request(requestId, opType, arguments);
    }

    // method to marshal response sent by the server
    public static byte[] marshalServerResponse(Response resp) {

        // retrieve the status of execution and the bytes of the text message
        int statusType = resp.getStatus().ordinal();
        String messageString = resp.getMessage();
        byte[] messageStringBytes = messageString.getBytes(StandardCharsets.UTF_16);

        int contentLen = 2 * Integer.BYTES + messageStringBytes.length;
        ByteBuffer marshalledServerResponse = ByteBuffer.allocate(contentLen);

        marshalledServerResponse
                .putInt(statusType)
                .putInt(messageStringBytes.length)
                .put(messageStringBytes);

        // return the marshalled response
        return marshalledServerResponse.array();
    }

    // method to unmarshal server response at client side
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

        // return the unmarshalled message of type response
        return new Response(statusValues[statusType], message);
    }
}
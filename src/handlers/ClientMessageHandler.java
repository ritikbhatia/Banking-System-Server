package handlers;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

import system.message.*;

public class ClientMessageHandler {

    public static final byte UPCOMING_STRING = 0x01;
    public static final byte UPCOMING_INT = 0x02;
    public static final byte UPCOMING_FLOAT = 0x03;

    public static Request unmarshal(byte[] packetData) {
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
    public static byte[] marshal(Response resp) {
        return new byte[1024];
    }

    /**
     * 
     * do we also need separate marshalling for message from client and
     * unmarshalling of message on client side?
     */
}
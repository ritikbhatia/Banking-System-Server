package client;

import java.net.*;
import java.io.*;
import java.lang.Exception;
import bank.entities.OpType;
import handlers.MessageHandler;
import system.message.*;
import system.message.Response.Status;

public class ClientInterface {

    DatagramSocket ds;
    InetAddress serverIp;
    int serverPort;
    static int requestId;

    private boolean simulate;
    private double clientLossRate;

    public ClientInterface(int port, boolean simulate, double clientLossRate) {
        try {
            ds = new DatagramSocket();
            ds.setSoTimeout(10000);
            serverPort = port;
            requestId = 0;
            this.simulate = simulate;
            this.clientLossRate = clientLossRate;

        } catch (Exception e) {
            System.out.println("SOCKET EXCEPTION RAISED!!" + e);
        }

    }

    // This function is used to set the Server IP
    public void setServerIp(InetAddress ip) {
        serverIp = ip;
    }

    /*
     * This function received the organised input from the user, sends it to
     * ClientMessageHandlerPython
     * to be marshalled and then sends the marshaled data to sendRequest function to
     * be sent to the Server.
     */
    public Response startService(Object[] contentObjects, int OpType) {
        Response response = null;
        Request request = new Request(requestId, OpType,
                contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        int retry = 2;
        for (int i = 1; i <= retry; i++) {
            sendRequest(serverIp, serverPort, content);
            response = receiveResponse();// return reply.getContent();
            if (response == null)
                continue;
            break;
        }
        return response;
    }

    // This function is used to send the monitoring request to the Server.
    public Response monitorUpdates(Object[] contentObjects) {
        Request request = new Request(requestId, OpType.MONITOR_UPDATES.getCode(), contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(serverIp, serverPort, content);
        int interval = (int) contentObjects[0];
        long elapsed = System.nanoTime();
        while ((System.nanoTime() - elapsed) / 1e9 <= interval) {
            Response response = receiveResponse();
            if (response != null) {
                System.out.println("Update: " + response.getMessage());
            }
        }

        return (new Response(Status.SUCCESS, "Monitoring period complete!"));
    }

    // This function is used to send the Request to the Server
    public void sendRequest(InetAddress address, int port, byte[] requestBytes) {

        if (simulate && Math.random() < clientLossRate) {
            System.out.println("Simulating client loss");
            return;
        }

        int retry = 0;
        while (retry < 3) {
            try {
                DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, address, port);
                ds.send(packet);
                break;
            } catch (SocketTimeoutException ste) {
                retry++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // This function is used to receive the Response from the Server
    public Response receiveResponse() {
        byte[] responseBytes = new byte[10240];
        DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length);

        while (true) {
            try {
                ds.receive(packet);
                break;
            } catch (SocketTimeoutException e) {
                return (null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] msg = packet.getData();
        Response response = MessageHandler.unmarshalServerResponse(msg);
        requestId++;
        return response;
    }

}

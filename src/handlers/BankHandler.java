package handlers;

import java.io.IOException;
import java.net.*;
import java.util.*;

import bank.entities.OpType;
import system.*;
import system.message.*;

public class BankHandler implements Runnable {
    DatagramSocket socketConn;
    private static HashMap<Integer, Response> responsesSent;
    private boolean at_most_once;
    private Server server;

    public BankHandler(int port, boolean at_most_once) {
        try {
            socketConn = new DatagramSocket(port);
            socketConn.setSoTimeout(6000);
            this.at_most_once = at_most_once;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decodePacket(DatagramPacket clientPacket) {
        byte[] packetData = clientPacket.getData();
        int clientPort = clientPacket.getPort();
        InetAddress clientIP = clientPacket.getAddress();

        Request clientRequest = MessageHandler.unmarshal(packetData);
        OpType operation = OpType.createFromType(clientRequest.getType());

        Response reply;
        if (at_most_once) {
            reply = responsesSent.get(clientRequest.getId());
            if (reply != null) {
                send(clientIP, clientPort, MessageHandler.marshal(reply));
                return;
            }
        }

        // TODO: invoke method and design reply message here
        reply = invokeService(operation);

        if (at_most_once) {
            responsesSent.put(clientRequest.getId(), reply);
        }

        send(clientIP, clientPort, MessageHandler.marshal(reply));
        informSubscribers(reply);
    }

    private Response invokeService(OpType operation) {
        return new Response();
    }

    private void send(InetAddress clientIP, int clientPort, byte[] msg) {
        for (int i = 0; i < 3; i++) {
            try {
                DatagramPacket replyPacket = new DatagramPacket(msg, msg.length, clientIP, clientPort);
                socketConn.send(replyPacket);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Retrying sending message to client");
            }
        }
    }

    private void informSubscribers(Response resp) {
        List<Subscriber> subscribers = server.getSubscribers();
        for (Subscriber sub : subscribers) {
            send(sub.getIP(), sub.getPort(), MessageHandler.marshal(resp));
        }
    }

    @Override
    public void run() {
        byte[] clientData = new byte[1024];
        while (true) {
            try {
                DatagramPacket clientDataPacket = new DatagramPacket(clientData, clientData.length);
                socketConn.receive(clientDataPacket);
                decodePacket(clientDataPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
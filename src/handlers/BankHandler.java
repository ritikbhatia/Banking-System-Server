package handlers;

import java.net.*;
import java.util.*;

import bank.entities.*;
import system.*;
import system.message.*;
import system.message.Response.Status;

public class BankHandler implements Runnable {
    DatagramSocket socketConn;
    private static HashMap<Integer, Response> responsesSent = new HashMap<Integer, Response>();
    private boolean at_most_once;
    private Bank bank;

    private boolean simulate = false;
    private double serverLossRate = 0.0;

    public BankHandler(int port, boolean at_most_once, Bank bank, boolean simulate, double lossRate) {
        try {
            socketConn = new DatagramSocket(port);
            socketConn.setSoTimeout(60000);
            this.at_most_once = at_most_once;
            this.bank = bank;
            this.simulate = simulate;
            this.serverLossRate = lossRate;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decodePacket(DatagramPacket clientPacket) {
        byte[] packetData = clientPacket.getData();
        int clientPort = clientPacket.getPort();
        InetAddress clientIP = clientPacket.getAddress();

        Request clientRequest = MessageHandler.unmarshalClientRequest(packetData);
        OpType operation = OpType.createFromType(clientRequest.getType());
        Object[] arguments = clientRequest.getArguments();

        Response reply;

        if (at_most_once) {
            reply = responsesSent.get(clientRequest.getId());
            if (reply != null) {
                send(clientIP, clientPort, MessageHandler.marshalServerResponse(reply));
                return;
            }
        }

        // send request to the bank for execution
        reply = bank.serve(operation, arguments);

        // TODO: What is montioringRequestID?
        // TODO: Where to get monitoringInterval?
        if (operation.equals(OpType.MONITOR_UPDATES) && (reply.getStatus().equals(Status.SUCCESS))) {
            Subscriber subscriber = new Subscriber(clientIP, clientPort, 0, 10);
            bank.addSubscriber(subscriber);
        }

        if (at_most_once) {
            responsesSent.put(clientRequest.getId(), reply);
        }

        send(clientIP, clientPort, MessageHandler.marshalServerResponse(reply));
        informSubscribers(reply);
    }

    // TODO: Where to put the simulate flag? (inside the loop?)
    private void send(InetAddress clientIP, int clientPort, byte[] msg) {
        if (simulate && (Math.random() < serverLossRate)) {
            System.out.println("Simulating server loss");
            return;
        }

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
        List<Subscriber> subscribers = bank.getSubscribers();
        for (Subscriber sub : subscribers) {
            send(sub.getIP(), sub.getPort(), MessageHandler.marshalServerResponse(resp));
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
package handlers;

import java.net.*;
import java.util.*;

import bank.entities.*;
import system.*;
import system.message.*;
import system.message.Response.Status;

public class BankHandler implements Runnable {
    DatagramSocket socketConn;
    private static HashMap<String, Response> responsesSent = new HashMap<String, Response>();
    private boolean at_most_once;
    private Bank bank;

    private boolean simulate = false;
    private double serverLossRate = 0.0;

    public BankHandler(int port, boolean at_most_once, Bank bank, boolean simulate, double lossRate) {
        try {
            socketConn = new DatagramSocket(port);
            socketConn.setSoTimeout(60000000);
            this.at_most_once = at_most_once;
            this.bank = bank;
            this.simulate = simulate;
            this.serverLossRate = lossRate;
            linebreaker(45);
            System.out.println("Server details:");
            System.out.println("Server Port: " + Integer.valueOf(port));
            System.out.println("At-most-once: " + Boolean.valueOf(at_most_once));
            System.out.println("Simulating packet loss: " + Boolean.valueOf(simulate));
            linebreaker(45);

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

        System.out.println();
        System.out.println("Client request of type " + Integer.valueOf(operation.getCode()) + " received.");
        System.out.println();

        if (simulate && (operation == OpType.WITHDRAW_MONEY || operation == OpType.CREATE_ACCOUNT))
            return;

        Object[] arguments = clientRequest.getArguments();

        Response reply;

        if (at_most_once) {
            reply = responsesSent.get(Integer.valueOf(clientRequest.getId()) + clientIP.getHostAddress());
            if (reply != null) {
                System.out.println("Duplicate request id detected...");
                System.out.println("Replying with cached response as per at-most-once protocol");
                System.out.println();
                send(clientIP, clientPort, MessageHandler.marshalServerResponse(reply));
                return;
            }
        }

        // send request to the bank for execution
        reply = bank.serve(operation, arguments);
        System.out.println("Server status: " + reply.getMessage());
        System.out.println();

        // TODO: What is montioringRequestID?
        // TODO: Where to get monitoringInterval?
        if (operation.equals(OpType.MONITOR_UPDATES) && (reply.getStatus().equals(Status.SUCCESS))) {
            Subscriber subscriber = new Subscriber(clientIP, clientPort, 0, 10);
            bank.addSubscriber(subscriber);
            System.out.println("Subscriber with IP: " + clientIP.getHostAddress() + " and Port: "
                    + Integer.valueOf(clientPort) + " successfully registered!");
            System.out.println();
        }

        if (at_most_once) {
            responsesSent.put(Integer.valueOf(clientRequest.getId()) + clientIP.getHostAddress(), reply);
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
                System.out.println("Socket Exception! Retrying sending message to client...");
            }
        }
    }

    private void informSubscribers(Response resp) {
        linebreaker(45);
        System.out.println("Informing subscribers...");
        System.out.println();
        List<Subscriber> subscribers = bank.getSubscribers();
        List<Subscriber> invalidSubscribers = new ArrayList<Subscriber>();

        for (Subscriber sub : subscribers) {
            if (sub.withinMonitoringInterval()) {
                send(sub.getIP(), sub.getPort(), MessageHandler.marshalServerResponse(resp));
                System.out.println("Informed subscriber with IP: " + sub.getIP().getHostAddress() + " sucessfully");
            } else {
                invalidSubscribers.add(sub);
            }
        }

        // remove subscribers whose monitoring interval has terminated
        bank.purgeInvalidSubscribers(invalidSubscribers);

        if (bank.getSubscribers().size() == 0) {
            System.out.println("No subscribers to inform!");
        }

        linebreaker(45);
        linebreaker(45);
    }

    // helper function for pretty printing on the terminal
    private void linebreaker(int length) {
        for (int i = 0; i < length; i++)
            System.out.print('-');
        System.out.println();
    }

    @Override
    public void run() {
        byte[] clientData = new byte[10240];
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
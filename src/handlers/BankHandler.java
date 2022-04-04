package handlers;

import java.net.*;
import java.util.*;

import bank.entities.*;
import system.*;
import system.message.*;
import system.message.Response.Status;

// the BankHadnler class implements the Runnable interface to begin a thread
public class BankHandler implements Runnable {

    // defining class variables
    DatagramSocket socketConn;
    private static HashMap<String, Response> responsesSent = new HashMap<String, Response>();
    private boolean at_most_once;
    private Bank bank;
    private boolean allowThroughSimulate;
    private boolean simulate = false;
    private double serverLossRate = 0.0;

    // parameterized constructor to instantiate the class
    public BankHandler(int port, boolean at_most_once, Bank bank, boolean simulate, double lossRate) {
        try {

            // start a socket connection with a big timeout
            socketConn = new DatagramSocket(port);
            socketConn.setSoTimeout(60000000);

            // initialize the class variables
            this.at_most_once = at_most_once;
            this.bank = bank;
            this.simulate = simulate;
            this.serverLossRate = lossRate;
            this.allowThroughSimulate = false;

            // printing server details before incoming requests from clients
            linebreaker(45);
            System.out.println("Server details:");
            System.out.println("Server IP: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("Server Port: " + Integer.valueOf(port));
            System.out.println("At-most-once: " + Boolean.valueOf(at_most_once));
            System.out.println("Simulating packet loss: " + Boolean.valueOf(simulate));
            linebreaker(45);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // function to decode the datagram packet received from the client
    private void decodePacket(DatagramPacket clientPacket) {

        // retrieve the required information from the data packet such as client ip, the
        // request type, arguments etc.
        byte[] packetData = clientPacket.getData();
        int clientPort = clientPacket.getPort();
        InetAddress clientIP = clientPacket.getAddress();

        Request clientRequest = MessageHandler.unmarshalClientRequest(packetData);
        OpType operation = OpType.createFromType(clientRequest.getType());

        System.out.println();
        System.out.println("Client request of type " + operation.name() + " received.");
        System.out.println();

        Object[] arguments = clientRequest.getArguments();

        Response reply;

        // if the at-most-once semantics are used, return cached response if same
        // request id is encountered
        if (at_most_once) {

            // the key of the hashmap is combination of request id and client ip address
            reply = responsesSent.get(Integer.valueOf(clientRequest.getId()) + clientIP.getHostAddress());

            // if duplicate request detected, return cached response
            if (reply != null) {
                System.out.println("Duplicate request detected...");
                System.out.println("Replying with cached response as per at-most-once protocol");
                System.out.println();
                flipAllowFlag();
                send(clientIP, clientPort, MessageHandler.marshalServerResponse(reply));
                informSubscribers(reply);
                return;
            }
        }

        // send request to the bank for execution and get the response
        reply = bank.serve(operation, arguments);
        System.out.println("Server status: " + reply.getMessage());
        System.out.println();

        // if the client wants to monitor updates, record ip in the subscribers list
        if (operation.equals(OpType.MONITOR_UPDATES) && (reply.getStatus().equals(Status.SUCCESS))) {
            Subscriber subscriber = new Subscriber(clientIP, clientPort, 0, (Integer) arguments[0]);
            bank.addSubscriber(subscriber);
            System.out.println("Subscriber with IP: " + clientIP.getHostAddress() + " and Port: "
                    + Integer.valueOf(clientPort) + " successfully registered!");
            System.out.println();
        }

        // if at-most-once semantics are used, cache response for future use
        if (at_most_once) {
            responsesSent.put(Integer.valueOf(clientRequest.getId()) + clientIP.getHostAddress(), reply);
        }

        // if simulating packet loss, return without sending response
        // for demonstration, packet loss is simulated only for CLOSE_ACCOUNT and
        // DEPOSIT_MONEY
        if (operation == OpType.CLOSE_ACCOUNT || operation == OpType.DEPOSIT_MONEY) {
            if (simulate && !allowThroughSimulate) {
                flipAllowFlag();
                return;
            }
            flipAllowFlag();
        }

        // return the response to the client and infrom subscribers regarding the update
        send(clientIP, clientPort, MessageHandler.marshalServerResponse(reply));
        informSubscribers(reply);
    }

    // helper method to send the datagram packet to the client using socket
    // connection
    private void send(InetAddress clientIP, int clientPort, byte[] msg) {
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

    // method to inform subscribers regarding latest update
    private void informSubscribers(Response resp) {

        // only inform subscribers if it is a successful transaction
        if (resp.getStatus() == Status.SUCCESS) {
            linebreaker(45);
            System.out.println("Informing subscribers...");
            System.out.println();

            // get the list of subscribers from the bank
            List<Subscriber> subscribers = bank.getSubscribers();

            // list to track subscribers whose monitoring interval is over
            List<Subscriber> invalidSubscribers = new ArrayList<Subscriber>();

            for (Subscriber sub : subscribers) {
                // if the subscriber's monitoring interval is not complete, send them update
                if (sub.withinMonitoringInterval()) {
                    send(sub.getIP(), sub.getPort(), MessageHandler.marshalServerResponse(resp));
                    System.out.println("Informed subscriber with IP: " + sub.getIP().getHostAddress() + " sucessfully");
                } else {
                    // otherwise, add to the list of invalid subscribers
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
    }

    // helper function for pretty printing on the terminal
    private void linebreaker(int length) {
        for (int i = 0; i < length; i++)
            System.out.print('-');
        System.out.println();
    }

    // helper function to flip whether server should send response to client or not
    // in our simulation, the server doesn't response only once
    // if same request is received, it then sends the response
    private void flipAllowFlag() {
        allowThroughSimulate = !allowThroughSimulate;
    }

    // override the run() method of the thread
    @Override
    public void run() {
        byte[] clientData = new byte[10240];
        while (true) {
            try {
                // keep receiving client packets
                DatagramPacket clientDataPacket = new DatagramPacket(clientData, clientData.length);
                socketConn.receive(clientDataPacket);
                decodePacket(clientDataPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
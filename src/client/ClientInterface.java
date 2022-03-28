package client;

import java.net.*;
import java.io.*;
import java.lang.Exception;
import bank.entities.Account;
import bank.entities.OpType;
import handlers.MessageHandler;
import system.message.*;
import system.message.Response.Status;

public class ClientInterface {

    DatagramSocket ds;
    InetAddress serverIP;
    int serverPort;
    static int requestID;

    private boolean simulate;
    private double clientLossRate;

    public ClientInterface(int port, boolean simulate, double clientLossRate) {
        try {
            ds = new DatagramSocket();
            serverPort = port;
            requestID = 0;
            this.simulate = simulate;
            this.clientLossRate = clientLossRate;

        } catch (Exception e) {
            System.out.println("SOCKET EXCEPTION RAISED!!" + e);
        }

    }

    public void setServerIP(InetAddress ip) {
        serverIP = ip;
    }

    // private Object[] getAccountDetails(Account account) {
    //     Object[] accountDetails = new Object[4];
    //     accountDetails[0] = account.getAccountHolderName();
    //     accountDetails[1] = account.getPassword();
    //     accountDetails[2] = account.getCurrency().getId();
    //     accountDetails[3] = account.getBalance();
    //     return accountDetails;
    // }

    // private Object[] getCombinedAccountAndAmount(Account accountFrom, Account accountTo, int amount) {
    //     Object[] combined = new Object[9];
    //     Object[] accountFromDetails = getAccountDetails(accountFrom);
    //     Object[] accountToDetails = getAccountDetails(accountTo);
    //     for (int i = 0; i < 4; i++) {
    //         combined[i] = accountFromDetails[i];
    //     }

    //     for (int i = 0; i < 4; i++) {
    //         combined[i + 4] = accountToDetails[i];
    //     }

    //     combined[8] = amount;
    //     return combined;
    // }

    // private Object[] getAccountAndAmount(Account account, int amount) {
    //     Object[] accountDetails = getAccountDetails(account);
    //     Object[] accountAndAmount = new Object[5];
    //     for (int i = 0; i < 4; i++) {
    //         accountAndAmount[i] = accountDetails[i];
    //     }
    //     accountAndAmount[4] = amount;
    //     return accountAndAmount;
    // }

    public Response openAccount(Object[] contentObjects) {
        Request request = new Request(requestID, OpType.CREATE_ACCOUNT.getCode(),
                contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(serverIP, serverPort, content);
        Response response = receiveResponse();// return reply.getContent();
        return response;
    }

    public Response closeAccount(Object[] contentObjects) {
        Request request = new Request(requestID, OpType.CLOSE_ACCOUNT.getCode(), contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(serverIP, serverPort, content);
        Response response = receiveResponse();
        return response;
    }

    public Response depositMoney(Object[] contentObjects) {
        Request request = new Request(requestID, OpType.DEPOSIT_MONEY.getCode(), contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(serverIP, serverPort, content);
        Response response = receiveResponse();
        return response;
    }

    public Response withdrawMoney(Object[] contentObjects) {
        Request request = new Request(requestID, OpType.WITHDRAW_MONEY.getCode(),contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(serverIP, serverPort, content);
        Response response = receiveResponse();
        return response;
    }

    public Response transferMoney(Object[] contentObjects) {
        Request request = new Request(requestID, OpType.TRANSFER_MONEY.getCode(),
        contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(serverIP, serverPort, content);
        Response response = receiveResponse();
        return response;
    }

    public Response transactionHistory(Object[] contentObjects) {
        Request request = new Request(requestID, OpType.TRANSACTION_HISTORY.getCode(), contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(serverIP, serverPort, content);
        Response response = receiveResponse();
        return response;

    }

    public Response monitorUpdates(Object[] contentObjects) {
        Request request = new Request(requestID, OpType.MONITOR_UPDATES.getCode(), contentObjects);
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(serverIP, serverPort, content);
        int interval = (int) contentObjects[0];
        long ellapsed = System.nanoTime();
        while((System.nanoTime()-ellapsed)/1e9<=interval)
        {
            Response response = receiveResponse();
            return response;
    }
    return(new Response(Status.SUCCESS,"Successful"));
}

    // TODO: Where to put the simulate flag? (inside the loop?)
    public void sendRequest(InetAddress address, int port, byte[] requestBytes) {
        ++requestID;

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

    public Response receiveResponse() {
        byte[] responseBytes = new byte[10240];
        DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length);
        while (true) {
            try {
                ds.receive(packet);
                break;
            } catch (SocketTimeoutException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] msg = packet.getData();
        Response response = MessageHandler.unmarshalServerResponse(msg);
        return response;
    }

    // DatagramSocket ds = new DatagramSocket();
    // InetAddress ip = InetAddress.getLocalHost();
    // String message = "Random Crap";
    // byte buf[] = null;
    // buf = message.getBytes();
    // DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1234);
    // ds.send(DpSend);

    // // loop while user not enters "bye"
    // while (true) {
    // String inp = in.readLine();
    // if (message.equals("bye"))
    // break;
    // }
    // ds.close();
}

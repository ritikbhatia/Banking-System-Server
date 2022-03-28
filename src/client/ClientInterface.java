package client;

import java.net.*;
import java.io.*;
import java.lang.Exception;
import bank.entities.Account;
import bank.entities.OpType;
import handlers.MessageHandler;
import system.message.*;

public class ClientInterface {

    DatagramSocket ds;
    InetAddress server_ip;
    int server_port;
    static int request_id;

    private boolean simulate;
    private double clientLossRate;

    public ClientInterface(int port, boolean simulate, double clientLossRate) {
        try {
            ds = new DatagramSocket();
            server_port = port;
            request_id = 0;
            this.simulate = simulate;
            this.clientLossRate = clientLossRate;

        } catch (Exception e) {
            System.out.println("SOCKET EXCEPTION RAISED!!" + e);
        }

    }

    public void set_server_ip(InetAddress ip) {
        server_ip = ip;
    }

    public Response openAccount(Account account) {
        // Object[] accountDetails = new Object[5];
        // accountDetails[0] = account.getAccountNumber();
        // accountDetails[1] = account.getAccountHolderName();
        // accountDetails[2] = account.getPassword();
        // accountDetails[3] = account.getCurrency();
        // accountDetails[4] = account.getBalance();

        // Request request = new Request(request_id, OpType.CREATE_ACCOUNT.getCode(),
        // accountDetails);
        Request request = new Request(request_id, OpType.CREATE_ACCOUNT.getCode(), new Object[] { account });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();// return reply.getContent();
        return response;
    }

    public Response closeAccount(Account account) {
        Request request = new Request(request_id, OpType.CLOSE_ACCOUNT.getCode(), new Object[] { account });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    public Response depositMoney(Account account, int amount) {
        Request request = new Request(request_id, OpType.DEPOSIT_MONEY.getCode(), new Object[] { account, amount });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    public Response withdrawMoney(Account account, int amount) {
        Request request = new Request(request_id, OpType.WITHDRAW_MONEY.getCode(), new Object[] { account, amount });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    public Response transferMoney(Account accountFrom, Account accountTo, int amount) {
        Request request = new Request(request_id, OpType.TRANSFER_MONEY.getCode(),
                new Object[] { accountFrom, accountTo, amount });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    public Response transactionHistory(Account account) {
        Request request = new Request(request_id, OpType.TRANSACTION_HISTORY.getCode(), new Object[] { account });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    public Response monitorUpdates(Account account) {
        Request request = new Request(request_id, OpType.MONITOR_UPDATES.getCode(), new Object[] { account });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    // TODO: Where to put the simulate flag? (inside the loop?)
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

    public Response receiveResponse() {
        byte[] responseBytes = new byte[1024];
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

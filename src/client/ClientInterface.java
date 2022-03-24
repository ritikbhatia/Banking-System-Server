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

    ClientInterface(int port) {
        try {
            ds = new DatagramSocket();
            server_port = port;
            request_id = 0;
        } catch (Exception e) {
            System.out.println("SOCKET EXCEPTION RAISED!!" + e);
        }

    }

    void set_server_ip(InetAddress ip) {
        server_ip = ip;
    }

    Response openAccount(Account account) {
        Request request = new Request(request_id, OpType.CREATE_ACCOUNT.getCode(), new Object[] { account });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();// return reply.getContent();
        return response;
    }

    Response closeAccount(Account account) {
        Request request = new Request(request_id, OpType.CLOSE_ACCOUNT.getCode(), new Object[] { account });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    Response depositMoney(Account account, int amount) {
        Request request = new Request(request_id, OpType.DEPOSIT_MONEY.getCode(), new Object[] { account, amount });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    Response withdrawMoney(Account account, int amount) {
        Request request = new Request(request_id, OpType.WITHDRAW_MONEY.getCode(), new Object[] { account, amount });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    Response transferMoney(Account accountFrom, Account accountTo, int amount) {
        Request request = new Request(request_id, OpType.TRANSFER_MONEY.getCode(),
                new Object[] { accountFrom, accountTo, amount });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    Response transactionHistory(Account account) {
        Request request = new Request(request_id, OpType.TRANSACTION_HISTORY.getCode(), new Object[] { account });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    Response monitorUpdates(Account account) {
        Request request = new Request(request_id, OpType.MONITOR_UPDATES.getCode(), new Object[] { account });
        byte[] content = MessageHandler.marshalClientRequest(request);
        sendRequest(server_ip, server_port, content);
        Response response = receiveResponse();
        return response;
    }

    void sendRequest(InetAddress address, int port, byte[] requestBytes) {
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

    Response receiveResponse() {
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

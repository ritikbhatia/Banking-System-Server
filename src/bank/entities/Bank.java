package bank.entities;

import java.util.*;

import system.message.Response;
import bank.services.*;
import system.Subscriber;

// class to define the bank
public class Bank {

    private HashMap<Integer, Account> accounts;
    private HashMap<OpType, Service> services;
    private List<Subscriber> subscribers;

    public Bank() {
        accounts = new HashMap<Integer, Account>();
        services = new HashMap<>();
        subscribers = new ArrayList<Subscriber>();
    }

    // helper methods below to add and remove components of the bank
    public void addService(OpType operation, Service service) {
        services.put(operation, service);
        service.setBank(this);
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account.getAccountNumber());
    }

    public int idExists(int id) {
        return accounts.containsKey(id) ? 1 : 0;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    // method to remove subscribers who monitoring period has expired
    public void purgeInvalidSubscribers(List<Subscriber> invalidSubscribers) {
        for (Subscriber invalidSubscriber : invalidSubscribers) {
            subscribers.remove(invalidSubscriber);
        }
    }

    public Account getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean checkAccountNumberExists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    // method to server the operation requested by the client
    public Response serve(OpType op, Object... params) {

        switch (op) {

            // call the relevant method to serve client request
            case CREATE_ACCOUNT: {
                OpenAccount service = (OpenAccount) this.services.get(op);
                return service.openAccount((String) params[0], (String) params[1], (Integer) params[2],
                        (Double) params[3]);
            }

            case CLOSE_ACCOUNT: {
                CloseAccount service = (CloseAccount) this.services.get(op);
                return service.closeAccount((String) params[0], (String) params[1], (Integer) params[2]);
            }

            case DEPOSIT_MONEY: {
                DepositMoney service = (DepositMoney) this.services.get(op);
                return service.depositMoney((String) params[0], (String) params[1],
                        (Integer) params[2], (Double) params[3], (Integer) params[4]);
            }

            case WITHDRAW_MONEY: {
                WithdrawMoney service = (WithdrawMoney) this.services.get(op);
                return service.withdrawMoney((String) params[0], (String) params[1],
                        (Integer) params[2], (Double) params[3], (Integer) params[4]);
            }

            case TRANSFER_MONEY: {
                TransferMoney service = (TransferMoney) this.services.get(op);
                return service.transferMoney((String) params[0], (String) params[1],
                        (Integer) params[2], (Integer) params[3], (Integer) params[4], (Double) params[5]);
            }

            case TRANSACTION_HISTORY: {
                TransactionHistory service = (TransactionHistory) this.services.get(op);
                return service.viewHistory((String) params[0], (String) params[1], (Integer) params[2]);
            }

            case MONITOR_UPDATES: {
                MonitorUpdates service = (MonitorUpdates) this.services.get(op);
                return service.monitorUpdates((Integer) params[0]);
            }

            // default response if an invalid operation type is received
            default:
                return new Response(Response.Status.ERROR, "Invalid operation");
        }

    }

}

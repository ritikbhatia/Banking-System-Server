package bank.entities;

import java.util.*;

import system.message.Response;
import bank.services.*;
import system.Subscriber;

public class Bank {

    private HashMap<Integer, Account> accounts;
    private HashMap<OpType, Service> services;
    private List<Subscriber> subscribers;

    public Bank() {
        accounts = new HashMap<Integer, Account>();
        services = new HashMap<>();
        subscribers = new ArrayList<Subscriber>();
    }

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

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public Account getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean checkAccountNumberExists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public Response serve(OpType op, Object... params) {

        switch (op) {
            case CREATE_ACCOUNT: {
                OpenAccount service = (OpenAccount) this.services.get(op);
                return service.openAccount((String) params[0], (String) params[1], (Integer) params[2],
                        (Double) params[3]);
            }

            case CLOSE_ACCOUNT: {
                CloseAccount service = (CloseAccount) this.services.get(op);
                return service.closeAccount((Integer) params[0], (String) params[1], (String) params[2]);
            }

            case DEPOSIT_MONEY: {
                DepositMoney service = (DepositMoney) this.services.get(op);
                return service.depositMoney((Integer) params[0], (String) params[1], (String) params[2],
                        (Integer) params[2], (Double) params[3]);
            }

            case WITHDRAW_MONEY: {
                WithdrawMoney service = (WithdrawMoney) this.services.get(op);
                return service.withdrawMoney((Integer) params[0], (String) params[1], (String) params[2],
                        (Integer) params[2], (Double) params[3]);
            }

            case TRANSFER_MONEY: {
                TransferMoney service = (TransferMoney) this.services.get(op);
                return service.transferMoney((Integer) params[0], (Integer) params[0], (String) params[1],
                        (String) params[2], (Integer) params[3], (Double) params[4]);
            }

            case TRANSACTION_HISTORY: {
                TransactionHistory service = (TransactionHistory) this.services.get(op);
                return service.viewHistory((Integer) params[0], (String) params[1], (String) params[2]);
            }

            // TODO: Is this montor updates OK?
            case MONITOR_UPDATES: {
                MonitorUpdates service = (MonitorUpdates) this.services.get(op);
                return service.monitorUpdates((Integer) params[0], (String) params[1], (String) params[2]);
            }

            default:
                return new Response(Response.Status.ERROR, "Invalid operation");
        }

    }

}
package bank.services;

import java.util.TreeMap;
import java.util.Formatter;
import java.sql.Timestamp;
import system.message.Response;
import system.message.Response.Status;
import bank.entities.*;

public class TransactionHistory extends Service {

    private String generateTransactionStatement(TreeMap<Timestamp, Transaction> transactions) {
        StringBuilder transactionStatement = new StringBuilder();
        Formatter formatter = new Formatter(transactionStatement);
        formatter.format("%25s%20s%20s%20s%20s%50s\n", "Time Stamp", "Operation", "Currency", "Amount", "Balance",
                "Information");
        for (Timestamp timestamp : transactions.keySet()) {
            Transaction transaction = transactions.get(timestamp);
            formatter.format("%25s%20s%20s%20s%20s%50s\n", transaction.getTimestamp(), transaction.getOperation(),
                    transaction.getCurrency(), transaction.getAmount(), transaction.getBalance(),
                    transaction.getInformation());
        }
        formatter.close();
        return transactionStatement.toString();
    }

    public Response viewHistory(String accountHolderName, String password, int accountNumber) {

        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);

        if (checkAccountResponse.getStatus() == Status.FAILURE) {
            return checkAccountResponse;
        }

        Account bankAccount = bank.getAccount(accountNumber);
        String mssg = generateTransactionStatement(bankAccount.getTranactionHistory());

        return new Response(Status.SUCCESS, mssg);
    }

}

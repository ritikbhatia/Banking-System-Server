package bank.services;

import java.util.TreeMap;
import java.sql.Timestamp;
import system.message.Response;
import system.message.Response.Status;
import bank.entities.*;

public class TransactionHistory extends Service {

    private String generateTransactionStatement(TreeMap<Timestamp, Transaction> transactions) {
        StringBuilder transactionStatement = new StringBuilder();
        transactionStatement.append("Time Stamp \t Operation \t Currency \t Amount \t Information \n");
        for (Timestamp timestamp : transactions.keySet()) {
            Transaction transaction = transactions.get(timestamp);
            transactionStatement.append(transaction.getTimestamp()).append("\t").append(transaction.getOperation()).append("\t").append(transaction.getCurrency()).append("\t").append(transaction.getAmount()).append("\t").append(transaction.getInformation()).append("\n");
        }
        return transactionStatement.toString();
    }

    public Response viewHistory(String accountHolderName, String password, int accountNumber) {

        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);
       
        if(checkAccountResponse.getStatus() == Status.FAILURE){
            return checkAccountResponse;
         }
 
        Account bankAccount = bank.getAccount(accountNumber);
        String mssg = generateTransactionStatement(bankAccount.getTranactionHistory()); 
        
        return new Response(Status.SUCCESS, mssg);
    }

}

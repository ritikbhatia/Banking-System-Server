package bank.services;

import bank.entities.*;
import system.message.Response;
import system.message.Response.Status;

public class TransferMoney extends Service {

    public TransferMoney() {
        super();
    }

    // Transfers money from account with given id, account name, and password
    // Return response with updated balance if successful

    public Response transferMoney(String accountHolderName, String password, int currencyCode, int accountNumber,
            int payeeAccountNumber, double amount) {

        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);
        Currency currency = Currency.fromId(currencyCode);

        if (checkAccountResponse.getStatus() == Status.FAILURE) {
            return checkAccountResponse;
        }

        if (bank.idExists(payeeAccountNumber) == 0) {
            String mssg = "Account number " + payeeAccountNumber + " does not exist.";
            return new Response(Status.FAILURE, mssg);
        }

        Account bankAccount = bank.getAccount(accountNumber);
        Account payeeAccount = bank.getAccount(payeeAccountNumber);

        if ((payeeAccount.getCurrency() != currency) || (bankAccount.getCurrency() != currency)) {
            String mssg = "Currency does not match.";
            return new Response(Status.FAILURE, mssg);
        }

        else if (bankAccount.getBalance() < amount) {
            String mssg = "Insufficient balance.";
            return new Response(Status.FAILURE, mssg);
        }

        else if (!bank.checkAccountNumberExists(payeeAccountNumber)) {
            String mssg = "Payee account number " + payeeAccountNumber + " does not exist.";
            return new Response(Status.FAILURE, mssg);
        }

        else {
            bankAccount.setBalance(bankAccount.getBalance() - amount);
            payeeAccount.setBalance(payeeAccount.getBalance() + amount);

            Transaction transaction = new Transaction(OpType.TRANSFER_MONEY, currency, amount, bankAccount.getBalance(),
                    "Money Transferred to " + payeeAccount.getAccountHolderName());
            bankAccount.addTransaction(transaction);

            Transaction receiverTransaction = new Transaction(OpType.TRANSFER_MONEY, currency, amount,
                    payeeAccount.getBalance(), "Money Recieved from " + bankAccount.getAccountHolderName());
            payeeAccount.addTransaction(receiverTransaction);

            String mssg = currency.toString() + " " + String.valueOf(amount) + " transferred from account "
                    + accountNumber + " to account " + payeeAccountNumber + " successfully.";
            return new Response(Status.SUCCESS, mssg);
        }
    }
}

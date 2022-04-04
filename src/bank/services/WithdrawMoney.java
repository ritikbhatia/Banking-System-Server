package bank.services;

import bank.entities.*;
import system.message.Response;
import system.message.Response.Status;

public class WithdrawMoney extends Service {

    // Withdraws money from account with given id, account name, and password
    // Returns response with updated balance if successful

    public Response withdrawMoney(String accountHolderName, String password, int currencyCode, double amount,
            int accountNumber) {

        Response checkResponse = checkAccountDetails(accountNumber, accountHolderName, password);
        Currency currency = Currency.fromId(currencyCode);

        if (checkResponse.getStatus() == Status.FAILURE) {
            return checkResponse;
        }

        Account bankAccount = bank.getAccount(accountNumber);

        if (bankAccount.getCurrency() != currency) {
            String mssg = "Currency does not match.";
            return new Response(Status.FAILURE, mssg);
        }

        else if (bankAccount.getBalance() < amount) {
            String mssg = "Insufficient balance.";
            return new Response(Status.FAILURE, mssg);
        }

        else {
            bankAccount.setBalance(bankAccount.getBalance() - amount);

            Transaction transaction = new Transaction(OpType.WITHDRAW_MONEY, currency, amount, bankAccount.getBalance(),
                    "Total Balance: " + bankAccount.getBalance());
            bankAccount.addTransaction(transaction);

            String mssg = currency.toString() + " " + String.valueOf(amount)
                    + " withdrawn from account successfully. New Balance: "
                    + bankAccount.getBalance();
            return new Response(Status.SUCCESS, mssg);
        }
    }

}

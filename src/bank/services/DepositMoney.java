package bank.services;

import bank.entities.*;
import system.message.Response;
import system.message.Response.Status;

public class DepositMoney extends Service {

    // Deposits money into account with given id, account name, and password
    // Returns response with updated balance if successful

    public Response depositMoney(String accountHolderName, String password, int currencyCode, double amount,
            int accountNumber) {

        Response checkAccountResponse = checkAccountDetails(accountNumber, accountHolderName, password);
        Currency currency = Currency.fromId(currencyCode);

        if (checkAccountResponse.getStatus() == Status.FAILURE) {
            return checkAccountResponse;
        }

        Account bankAccount = bank.getAccount(accountNumber);

        if (bankAccount.getCurrency() != currency) {
            String mssg = "Currency does not match.";
            return new Response(Status.FAILURE, mssg);
        }

        else {
            bankAccount.setBalance(bankAccount.getBalance() + amount);

            Transaction transaction = new Transaction(OpType.DEPOSIT_MONEY, currency, amount, bankAccount.getBalance(),
                    "Total Balance: " + bankAccount.getBalance());
            bankAccount.addTransaction(transaction);

            String mssg = currency.toString() + " " + String.valueOf(amount)
                    + " deposited into account successfully. New Balance is: " + bankAccount.getBalance();
            return new Response(Status.SUCCESS, mssg);
        }
    }
}

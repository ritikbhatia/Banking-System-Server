package client;

import java.io.*;
import java.net.*;
import bank.entities.OpType;
import bank.entities.Currency;
import system.message.*;

public class ClientMain {

    static InputStreamReader read = new InputStreamReader(System.in);
    static BufferedReader in = new BufferedReader(read);

    private static boolean SIMULATE = false;
    private static double CLIENT_LOSS_RATE = 0.0;

    public static void getOptions() {
        System.out.println("Enter choice from the menu below:");
        System.out.println("1. Open Account");
        System.out.println("2. Close Existing Account");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Transfer Money");
        System.out.println("6. View Transaction History");
        System.out.println("7. Monitor Updates");
        System.out.println("8. Exit");
    }

    public static void linebreaker(int length) {
        for (int i = 0; i < length; i++)
            System.out.print('-');
        System.out.println();
    }

    public static String getName() throws IOException {
        System.out.print("Enter the account holder's name: ");
        String accountHolderName = in.readLine();
        return (accountHolderName);
    }

    public static String getPassword() throws IOException {
        String password;
        while (true) {
            System.out.print("Enter the password: ");
            password = in.readLine();
            if (password.length() < 1) {
                System.out.println("Invalid Input!");
                continue;
            }
            break;
        }
        return (password);
    }

    public static int getCurrency() throws IOException {
        Currency currency;
        int length = 0;
        for (Currency cur : Currency.values()) {
            System.out.println(String.format("%d. %s", cur.getId(), cur.name()));
            length++;
        }
        while (true) {
            try {
                System.out.print("Select the currency: ");
                int choice = Integer.parseInt(in.readLine());
                if (choice > 0 && choice <= length) {
                    currency = Currency.fromId(choice);
                    break;
                } else {
                    System.out.println("Invalid Choice. Please choose again!");
                }
            } catch (Exception e) {
                System.out.println("Invalid Input!");
                continue;
            }
        }
        return currency.getId();
    }

    public static double getAccountBalance() throws IOException {
        double accountBalance;
        while (true) {
            try {
                System.out.print("Enter the balance amount: ");
                accountBalance = Double.parseDouble(in.readLine());
                if (accountBalance < 0) {
                    System.out.println("Invalid input. The balance amount cannot be negative!");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input!");
                continue;
            }
            break;
        }
        return (accountBalance);
    }

    public static int getAccountNumber() throws IOException {
        int accountNumber;
        while (true) {
            try {
                System.out.print("Enter the bank account number: ");
                accountNumber = Integer.parseInt(in.readLine());
            } catch (Exception e) {
                System.out.println("Invalid Input!");
                continue;
            }
            break;
        }
        return (accountNumber);
    }

    static void printResponse(Response response) {
        System.out.println();
        System.out.println(response.getMessage());
        System.out.println();
    }

    public static void main(String args[]) throws IOException {

        int serverPort = 6789; // Change this to change the server port number
        ClientInterface clientInterface = new ClientInterface(serverPort, SIMULATE, CLIENT_LOSS_RATE);
        clientInterface.setServerIP(InetAddress.getByName("RITIK-DELL-XPS"));

        System.out.println("Starting bank client...");
        System.out.println();

        while (true) {
            getOptions();
            int option;
            while (true) {
                try {
                    System.out.println();
                    System.out.print("Your choice: ");
                    option = Integer.parseInt(in.readLine());
                }

                catch (Exception e) {
                    System.out.println("Invalid Input. Please enter again!");
                    continue;
                }
                if (option >= 0 && option <= 8)
                    break;
            }

            if (option == 8) {
                System.out.print("The program has ended!");
                break;
            }
            System.out.println();
            double amount;
            String accountHolderName;
            String password;
            int currency;
            double accountBalance;
            int accountNumber;
            Object[] contentObject;
            Response response;
            switch (option) {

                case 1: // CREATE A NEW ACCOUNT
                    linebreaker(45);
                    System.out.println("Creating a new account");
                    linebreaker(45);
                    // account = getinputDetails(true, true, true, true, false);
                    accountHolderName = getName();
                    password = getPassword();
                    currency = getCurrency();
                    accountBalance = getAccountBalance();
                    contentObject = new Object[] { accountHolderName, password, currency, accountBalance };
                    response = clientInterface.startService(contentObject, OpType.CREATE_ACCOUNT.getCode());
                    // response = clientInterface.openAccount(contentObject);
                    printResponse(response);
                    break;

                case 2: // CLOSE ACCOUNT
                    linebreaker(45);
                    System.out.println("Closing the account");
                    linebreaker(45);
                    // account = getinputDetails(true, true, false, false, true);
                    accountHolderName = getName();
                    password = getPassword();
                    accountNumber = getAccountNumber();
                    contentObject = new Object[] { accountHolderName, password, accountNumber };
                    response = clientInterface.startService(contentObject, OpType.CLOSE_ACCOUNT.getCode());
                    // response = clientInterface.closeAccount(contentObject);
                    break;

                case 3: // DEPOSIT MONEY
                    linebreaker(45);
                    System.out.println("Depositing Money");
                    linebreaker(45);
                    // account = getinputDetails(true, true, true, false, true);
                    accountHolderName = getName();
                    password = getPassword();
                    currency = getCurrency();
                    accountNumber = getAccountNumber();
                    while (true) {
                        try {
                            System.out.print("Enter the amount to be deposited: ");
                            amount = Double.parseDouble(in.readLine());
                            if (amount <= 0) {
                                System.out.println("Invalid amount!");
                                continue;
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid Input!");
                            continue;
                        }
                        break;
                    }
                    contentObject = new Object[] { accountHolderName, password, currency, amount, accountNumber };
                    response = clientInterface.startService(contentObject, OpType.DEPOSIT_MONEY.getCode());
                    // response = clientInterface.depositMoney(contentObject);
                    printResponse(response);
                    break;

                case 4: // WITHDRAW MONEY
                    linebreaker(45);
                    System.out.println("Withdrawing money");
                    linebreaker(45);
                    // account = getinputDetails(true, true, true, false, true);
                    accountHolderName = getName();
                    password = getPassword();
                    currency = getCurrency();
                    accountNumber = getAccountNumber();
                    while (true) {
                        try {
                            System.out.print("Enter the amount to be withdrawn: ");
                            amount = Double.parseDouble(in.readLine());
                            if (amount <= 0) {
                                System.out.println("Invalid amount!");
                                continue;
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid Input!");
                            continue;
                        }
                        break;
                    }
                    contentObject = new Object[] { accountHolderName, password, currency, accountNumber, amount };
                    response = clientInterface.startService(contentObject, OpType.WITHDRAW_MONEY.getCode());
                    // response = clientInterface.withdrawMoney(contentObject);
                    printResponse(response);
                    break;

                case 5: // TRANSFER MONEY
                    linebreaker(45);
                    System.out.println("Transferring money");
                    linebreaker(45);

                    System.out.println();
                    System.out.println("Transferring from:");
                    // account = getinputDetails(true, true, true, true, false);
                    accountHolderName = getName();
                    password = getPassword();
                    currency = getCurrency();
                    accountNumber = getAccountNumber();

                    System.out.println();
                    System.out.println("Transferring to:");
                    int accountNumberTo = getAccountNumber();
                    // Account account_to = getinputDetails(false, false, false, false, true);
                    while (true) {
                        try {
                            System.out.println("Enter the amount to transferred");
                            amount = Double.parseDouble(in.readLine());
                            if (amount <= 0) {
                                System.out.println("Invalid amount!");
                                continue;
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid Input!");
                            continue;
                        }
                        break;
                    }
                    contentObject = new Object[] { accountHolderName, password, currency, accountNumber,
                            accountNumberTo, amount };

                    response = clientInterface.startService(contentObject, OpType.TRANSFER_MONEY.getCode());
                    // response = clientInterface.transferMoney(contentObject);
                    printResponse(response);
                    break;

                case 6: // VIEW TRANSACTION HISTORY
                    linebreaker(45);
                    System.out.println("Viewing transaction history");
                    linebreaker(45);

                    // account = getinputDetails(true, true, true, false, true);
                    accountHolderName = getName();
                    password = getPassword();
                    accountNumber = getAccountNumber();
                    contentObject = new Object[] { accountHolderName, password, accountNumber };
                    response = clientInterface.startService(contentObject, OpType.TRANSACTION_HISTORY.getCode());
                    // response = clientInterface.transactionHistory(contentObject);
                    printResponse(response);
                    break;

                case 7: // MONITOR UPDATES
                    linebreaker(45);
                    System.out.println("Monitoring Updates");
                    linebreaker(45);
                    int interval;
                    while (true) {
                        System.out.print("Enter the interval: ");
                        try {
                            interval = Integer.parseInt(in.readLine());
                        } catch (Exception e) {
                            System.out.println("Invalid Input!");
                            continue;
                        }
                        break;
                    }
                    System.out.println();
                    contentObject = new Object[] { interval };
                    response = clientInterface.monitorUpdates(contentObject);
                    printResponse(response);
                    break;

                default:
            }

        }
    }

}

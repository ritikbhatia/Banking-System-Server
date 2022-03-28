package client;

import java.io.*;
import java.net.*;

import bank.entities.Account;
import bank.entities.Currency;
import system.message.*;

public class ClientMain {

    static InputStreamReader read = new InputStreamReader(System.in);
    static BufferedReader in = new BufferedReader(read);

    private static boolean SIMULATE = false;
    private static double CLIENT_LOSS_RATE = 0.0;

    public static void options() {
        System.out.println("Enter your choice!");
        System.out.println("1. Open Account");
        System.out.println("2. Close Existing Account");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Transfer Money");
        System.out.println("6. View Transaction History");
        System.out.println("7. Monitor Updates");
        System.out.println("8. Exit!");
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

        int server_port = 8080; // Change this to change the server port number
        ClientInterface clientInterface = new ClientInterface(server_port, SIMULATE, CLIENT_LOSS_RATE);
        clientInterface.set_server_ip(InetAddress.getLocalHost()); // Change this to change the server IP Address

        System.out.println("Starting bank client...");

        while (true) {
            options();
            int option;
            while (true) {
                try {
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
            linebreaker(45);
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
                    System.out.println("Creating a new account.");
                    // account = getinputDetails(true, true, true, true, false);
                    accountHolderName = getName();
                    password = getPassword();
                    currency = getCurrency();
                    accountBalance = getAccountBalance();
                    contentObject = new Object[] { accountHolderName, password, currency, accountBalance };
                    response = clientInterface.openAccount(contentObject);
                    printResponse(response);
                    break;

                case 2: // CLOSE ACCOUNT
                    System.out.println("Closing the account.");
                    // account = getinputDetails(true, true, false, false, true);
                    accountHolderName = getName();
                    password = getPassword();
                    accountNumber = getAccountNumber();
                    contentObject = new Object[] { accountHolderName, password, accountNumber };
                    response = clientInterface.closeAccount(contentObject);
                    break;

                case 3: // DEPOSIT MONEY
                    System.out.println("Depositing Money.");
                    // account = getinputDetails(true, true, true, false, true);
                    accountHolderName = getName();
                    password = getPassword();
                    currency = getCurrency();
                    accountNumber = getAccountNumber();
                    while (true) {
                        try {
                            System.out.println("Enter the amount to be deposited");
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
                    response = clientInterface.depositMoney(contentObject);
                    printResponse(response);
                    break;

                case 4: // WITHDRAW MONEY
                    System.out.println("Withdrawing money.");
                    // account = getinputDetails(true, true, true, false, true);
                    accountHolderName = getName();
                    password = getPassword();
                    currency = getCurrency();
                    accountNumber = getAccountNumber();
                    while (true) {
                        try {
                            System.out.println("Enter the amount to be withdrawn");
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
                    response = clientInterface.withdrawMoney(contentObject);
                    printResponse(response);
                    break;

                case 5: // TRANSFER MONEY
                    System.out.println("Transferring money");
                    System.out.println("Transferring from:");
                    // account = getinputDetails(true, true, true, true, false);
                    accountHolderName = getName();
                    password = getPassword();
                    currency = getCurrency();
                    accountNumber = getAccountNumber();
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
                    response = clientInterface.transferMoney(contentObject);
                    printResponse(response);
                    break;

                case 6: // VIEW TRANSACTION HISTORY
                    System.out.println("Viewing transaction history.");

                    // account = getinputDetails(true, true, true, false, true);
                    accountHolderName = getName();
                    password = getPassword();
                    accountNumber = getAccountNumber();
                    contentObject = new Object[] { accountHolderName, password, accountNumber };
                    response = clientInterface.transactionHistory(contentObject);
                    printResponse(response);
                    break;

                // TODO: Need monitoring length
                case 7: // MONITOR UPDATES
                    System.out.println("Monitoring Updates.");
                    int interval;
                    while (true) {
                        System.out.println("Enter the interval");
                        try {
                            interval = Integer.parseInt(in.readLine());
                        } catch (Exception e) {
                            System.out.println("Invalid Input!");
                            continue;
                        }
                        break;
                    }
                    contentObject = new Object[] { interval };
                    response = clientInterface.monitorUpdates(contentObject);
                    printResponse(response);
                    break;

                default:
            }

        }
    }

}

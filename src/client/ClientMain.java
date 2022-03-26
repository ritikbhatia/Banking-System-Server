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

    public static Account getinputDetails(boolean iaccountHolderName, boolean ipassword, boolean icurrency,
            boolean iaccountBalance, boolean iaccountNumber)
            throws IOException {
        String accountHolderName = "";
        String password = "";
        Currency currency = Currency.DEFAULT_CURRENCY;
        double accountBalance = 0;
        int accountNumber = Account.DEFAULT_ACCOUNT_NUMBER;

        if (iaccountHolderName == true) {
            System.out.print("Enter the account holder's name: ");
            accountHolderName = in.readLine();
        }
        
        if (ipassword == true) {
            while (true) {
                try {
                    System.out.print("Enter the password: ");
                    password = in.readLine();
                    if (password.length() < 10) {
                        System.out.println("Error: Please enter a password of length atleast 10 characters!");
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid Input!");
                    continue;
                }
                break;
            }
        }

        if (icurrency == true) {
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

        }

        if (iaccountBalance == true) {
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
        }

        if (iaccountNumber == true) {
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
        }

        Account account = new Account(accountNumber, accountHolderName, password, currency, accountBalance);
        return (account);
    }

    static void printResponse(Response response){
        System.out.println("This is the responseeeee");
        System.out.println(response.getMessage());
    }
    public static void main(String args[]) throws IOException {

        int server_port = 1234; // Change this to change the server port number
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
            int amount;
            Account account;
            Response response;
            switch (option) {

                case 1: // CREATE A NEW ACCOUNT
                    System.out.println("Creating a new account.");
                    account = getinputDetails(true, true, true, true, false);
                    response = clientInterface.openAccount(account);
                    printResponse(response);
                    break;
                    
                case 2: // CLOSE ACCOUNT
                    System.out.println("Closing the account.");
                    account = getinputDetails(true, true, false, false, true);
                    response = clientInterface.closeAccount(account);
                    break;
                
                case 3: // DEPOSIT MONEY
                    System.out.println("Depositing Money.");
                    account = getinputDetails(true, true, true, false, true);
                    while (true) {
                        try {
                            System.out.println("Enter the amount to be deposited");
                            amount = Integer.parseInt(in.readLine());
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

                    response = clientInterface.depositMoney(account,amount);
                    printResponse(response);

                    break;

                case 4: // WITHDRAW MONEY
                    System.out.println("Withdrawing money.");
                    account = getinputDetails(true, true, true, false, true);
                    while (true) {
                        try {
                            System.out.println("Enter the amount to be withdrawn");
                            amount = Integer.parseInt(in.readLine());
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
                    response = clientInterface.withdrawMoney(account,amount);
                    printResponse(response);
                    break;

                case 5: // TRANSFER MONEY 
                    System.out.println("Transferring money");
                    System.out.println("Transferring from:");
                    account = getinputDetails(true, true, true, true, false);
                    System.out.println("Transferring to:");
                    Account account_to = getinputDetails(false, false, false, false, true);
                    while (true) {
                        try {
                            System.out.println("Enter the amount to transferred");
                            amount = Integer.parseInt(in.readLine());
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

                    response = clientInterface.transferMoney(account,account_to,amount);
                    printResponse(response);
                    break;

                case 6: // VIEW TRANSACTION HISTORY 
                    System.out.println("Viewing transaction history.");
                    account = getinputDetails(true, true, true, false, true);
                    response = clientInterface.transactionHistory(account);
                    printResponse(response);
                    break;

                //TODO: Need monitoring length 
                case 7: // MONITOR UPDATES
                    System.out.println("Monitoring Updates.");
                    account = getinputDetails(true, true, true, true, false);
                    response = clientInterface.monitorUpdates(account);
                    printResponse(response);
                    break;

                default:
            }

        }
    }

}

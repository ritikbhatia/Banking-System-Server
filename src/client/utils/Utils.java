package client.utils;

import system.message.*;

public class Utils {

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

    public static void printResponse(Response response) {
        System.out.println();
        System.out.println(response.getMessage());
        System.out.println();
    }
}

import bank.entities.*;
import bank.services.*;
import handlers.BankHandler;

// driver code for the server
public class Main {

    // constant initialization to configure the server
    private static int PORT = 6789;
    private static boolean AT_MOST_ONCE_FLAG = false;
    private static boolean SIMULATE = true;
    private static float SERVER_LOSS_RATE = 0;

    public static void main(String[] args) {
        try {

            // initialize the bank
            Bank bank = new Bank();

            // add services to the bank for the client to use
            bank.addService(OpType.CREATE_ACCOUNT, new OpenAccount());
            bank.addService(OpType.CLOSE_ACCOUNT, new CloseAccount());
            bank.addService(OpType.DEPOSIT_MONEY, new DepositMoney());
            bank.addService(OpType.WITHDRAW_MONEY, new WithdrawMoney());
            bank.addService(OpType.TRANSFER_MONEY, new TransferMoney());
            bank.addService(OpType.TRANSACTION_HISTORY, new TransactionHistory());
            bank.addService(OpType.MONITOR_UPDATES, new MonitorUpdates());

            // start the bank handler thread to server client requests
            System.out.println("Starting bank server...");
            System.out.println();
            BankHandler bankHandler = new BankHandler(PORT, AT_MOST_ONCE_FLAG, bank, SIMULATE, SERVER_LOSS_RATE);
            bankHandler.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
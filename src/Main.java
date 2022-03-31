import bank.entities.*;
import bank.services.*;
import handlers.BankHandler;

public class Main {

    private static int PORT = 6789;
    private static boolean AT_MOST_ONCE_FLAG = true;
    private static boolean SIMULATE = false;
    private static float SERVER_LOSS_RATE = 0;

    public static void main(String[] args) {
        try {
            Bank bank = new Bank();

            bank.addService(OpType.CREATE_ACCOUNT, new OpenAccount());
            bank.addService(OpType.CLOSE_ACCOUNT, new CloseAccount());
            bank.addService(OpType.DEPOSIT_MONEY, new DepositMoney());
            bank.addService(OpType.WITHDRAW_MONEY, new WithdrawMoney());
            bank.addService(OpType.TRANSFER_MONEY, new TransferMoney());
            bank.addService(OpType.TRANSACTION_HISTORY, new TransactionHistory());
            bank.addService(OpType.MONITOR_UPDATES, new MonitorUpdates());

            // starting bank handler
            System.out.println("Starting bank server...");
            System.out.println();
            BankHandler bankHandler = new BankHandler(PORT, AT_MOST_ONCE_FLAG, bank, SIMULATE, SERVER_LOSS_RATE);
            bankHandler.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
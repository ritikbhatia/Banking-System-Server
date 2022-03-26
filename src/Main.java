import bank.entities.*;
import bank.services.*;
import handlers.BankHandler;

public class Main {

    private static int PORT = 8080;
    private static boolean AT_MOST_ONCE_FLAG = true;

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
            BankHandler bankHandler = new BankHandler(PORT, AT_MOST_ONCE_FLAG, bank);
            System.out.println("Starting bank server...");
            bankHandler.run();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
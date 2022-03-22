import bank.entities.*;
import bank.services.*;

public class Main{
    public static void main(String[] args){

        Bank bank = new Bank();
        
        bank.addService(OpType.CREATE_ACCOUNT,  new OpenAccount());
        bank.addService(OpType.CLOSE_ACCOUNT, new CloseAccount());
        bank.addService(OpType.DEPOSIT_MONEY, new DepositMoney());
        bank.addService(OpType.WITHDRAW_MONEY, new WithdrawMoney());
        bank.addService(OpType.TRANSFER_MONEY, new TransferMoney());
    }
}
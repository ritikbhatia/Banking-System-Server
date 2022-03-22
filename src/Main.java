import entity.Bank;
import service.*;

public class Main{
    public static void main(String[] args){

        Bank bank = new Bank();
        
        bank.addService(new OpenAccount());
        bank.addService(new CloseAccount());
        bank.addService(new TransactMoney());
        bank.addService(new TransferMoney());
    }
}
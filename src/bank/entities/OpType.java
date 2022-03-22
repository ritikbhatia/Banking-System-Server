package bank.entities;

public enum OpType {
    CREATE_ACCOUNT(0), 
    CLOSE_ACCOUNT(1), 
    DEPOSIT_MONEY(2),
    WITHDRAW_MONEY(3),
    TRANSFER_MONEY(4),
    TRANSACTION_HISTORY(5);

  
    // declaring private variable for getting values
    private int opCode;
  
    // getter method
    public int getCode(){
        return this.opCode;
    }

    public static OpType createFromType(int opCode) {
        for (OpType type : OpType.values()) {
            if (type.getCode() == opCode) {
                return type;
            }
        }
        return null;
    }

    // enum constructor - cannot be public or protected
    private OpType(int opCode){
        this.opCode = opCode;
    }
}

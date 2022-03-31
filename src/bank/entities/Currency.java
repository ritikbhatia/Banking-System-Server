package bank.entities;

// enum for currency type
public enum Currency {

    // assume the existence of 3 currency types
    SGD(1), INR(2), USD(3);

    public static final Currency DEFAULT_CURRENCY = Currency.SGD;
    private int currency_id = 0;

    Currency(int code) {
        this.currency_id = code;
    }

    public int getId() {
        return this.currency_id;
    }

    // method to create Currency from the integer code
    public static Currency fromId(int code) {
        for (Currency cur : Currency.values()) {
            if (cur.currency_id == code)
                return cur;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
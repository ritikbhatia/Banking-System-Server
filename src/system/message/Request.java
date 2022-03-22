package system.message;

public class Request {
    private int id;
    private int opType;
    private Object[] arguments;

    public Request(int id, int opType, Object[] arguments) {
        this.id = id;
        this.opType = opType;
        this.arguments = arguments;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return opType;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
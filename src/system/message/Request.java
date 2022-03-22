package system.message;

public class Request {
    private int id;
    private int type;
    private Object[] arguments;

    public Request() {
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
package entity.message;

public class Response {

    public enum Status {
        FAILURE,
        SUCCESS
    }

    private Status status;
    private String message;

    public Response() {}

    public Response(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

package system.message;

// class to define the structure of the Response message
public class Response {

    // enum for status of the message
    public enum Status {
        FAILURE,
        SUCCESS,
        ERROR
    }

    private Status status;
    private String message;

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

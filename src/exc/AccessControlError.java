package exc;

public class AccessControlError extends Exception {

    public AccessControlError() {
        super("Access Control error");
    }

    public AccessControlError(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
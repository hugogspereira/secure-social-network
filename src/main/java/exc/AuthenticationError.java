package exc;

public class AuthenticationError extends Exception {

    public AuthenticationError() {
        super("Authentication error");
    }

    public AuthenticationError(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}

package exc;

public class NullParameterException extends Exception {

    public NullParameterException() {
        super("Null/Empty parameter(s)");
    }

    public NullParameterException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}

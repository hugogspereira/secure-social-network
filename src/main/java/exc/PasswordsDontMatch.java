package exc;

/**
 * PasswordsDontMatch exception
 * @author Hugo Pereira e Daniel Cavalheiro
 */
public class PasswordsDontMatch extends Exception {

    public PasswordsDontMatch() {
        super("Passwords do not match");
    }

    public PasswordsDontMatch(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
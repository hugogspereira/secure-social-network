package exc;

/**
 * AccountAlreadyExists exception
 * @author Hugo Pereira e Daniel Cavalheiro
 */
public class AccountAlreadyExists extends Exception {

    public AccountAlreadyExists() {
        super("Account already exists");
    }

    public AccountAlreadyExists(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
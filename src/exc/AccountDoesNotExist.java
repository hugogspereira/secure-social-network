package exc;

/**
 * AccountDoesNotExist exception
 * @author Hugo Pereira e Daniel Cavalheiro
 */
public class AccountDoesNotExist extends Exception {

    public AccountDoesNotExist() {
        super("Account does not exist");
    }

    public AccountDoesNotExist(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
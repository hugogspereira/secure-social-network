package exc;

public class AccountIsLocked extends Exception{

    public AccountIsLocked() {
        super("Account is locked");
    }

    public AccountIsLocked(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
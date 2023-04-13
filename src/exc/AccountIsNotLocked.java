package exc;

public class AccountIsNotLocked extends Exception{

    public AccountIsNotLocked() {
        super("Account is not locked");
    }

    public AccountIsNotLocked(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
package exc;

public class AccountIsLoggedIn extends Exception {

    public AccountIsLoggedIn() {
        super("Account is logged in");
    }

    public AccountIsLoggedIn(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}

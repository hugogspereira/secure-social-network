package acc;

/**
 * Account class - Represents a principal identity in the system.
 * @author Hugo Pereira e Daniel Cavalheiro
 */
public class Account implements Acc {

    private String accountName;
    private String password;

    // TODO: Add more fields if needed
    private boolean loggedIn; // true = user is logged in
    private boolean locked; // true = cannot authenticate

    /**
     * Account constructor
     * @param accountName the account name
     * @param password the account password
     */
    public Account(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;

        // Rethink if this fields need or not to be passed as parameters
        this.loggedIn = false;
        this.locked = false;
    }

    @Override
    public String getAccountName() {
        return accountName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
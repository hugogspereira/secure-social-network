package acc;

public interface Acc {

    /**
     * Get the account name
     * @return the account name
     */
    String getAccountName();

    /**
     * Get the account password
     * @return the account password
     */
    String getPassword();

    /**
     * Set the account password
     * @param password the new password for the account
     */
    void setPassword(String password);

    /**
     * Check if the account is logged in
     * @return true if the account is logged in, false otherwise
     */
    boolean isLoggedIn();

    /**
     * Check if the account is locked
     * @return true if the account is locked, false otherwise
     */
    boolean isLocked();

    /**
     * Set the account logged in status
     * @param loggedIn the new logged in status for the account
     */
    void setLoggedIn(boolean loggedIn);

    /**
     * Set the account locked status
     * @param locked the new locked status for the account
     */
    void setLocked(boolean locked);

    /**
     * Get the account JWT
     * @return the account JWT
     */
    String getJWT();
}
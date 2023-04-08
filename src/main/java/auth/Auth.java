package auth;

import acc.Acc;
import exc.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Auth {

    /**
     * Create a new account
     * @param name the account name
     * @param pwd1 the account password
     * @param pwd2 the account password confirmation
     * PRE: pwd1 == pwd2
     */
    void createAccount(String name, String pwd1, String pwd2) throws AccountAlreadyExists, PasswordsDontMatch, EncryptionDontWork, NullParameterException;

    /**
     * Delete an account
     * @param name the account name
     * PRE: the account cannot be logged in
     * PRE: the account must be locked
     */
    void deleteAccount(String name) throws AccountDoesNotExist, AccountIsLoggedIn, AccountIsNotLocked;

    /**
     * Get an account
     * @param name the account name
     * @return the account
     * @throws AccountDoesNotExist if the account does not exist
     */
    Acc getAccount(String name) throws AccountDoesNotExist;

    /**
     * Change an account password
     * @param name the account name
     * @param pwd1 the account password
     * @param pwd2 the account password confirmation
     */
    void changePwd(String name, String pwd1, String pwd2) throws NullParameterException, PasswordsDontMatch, EncryptionDontWork, AccountDoesNotExist;

    /**
     * Login an account
     * @param name the account name
     * @param pwd the account password
     * @return the account
     */
    Acc login(String name, String pwd) throws AccountDoesNotExist, AccountIsLocked, EncryptionDontWork, AuthenticationError;

    /**
     * Logout an account
     * PRE: name must identify an created account
     * PRE: pwd1 == pwd2
     * @param acc the account
     */
    void logout(Acc acc);

    /**
     * Authenticates the caller, given name and password
     *
     * • checks if name is defined as a account name
     * • if not, raise exception (UndefinedAccount)
     * • checks if account is locked
     * • if locked, raise exception (LockedAccount)
     * • compares the encryption of pwd with the stored hash
     * • if comparison succeeds, sets logged_in to true and
     * succeeds, returning the authenticated account
     * • otherwise raise exception (AuthenticationError)
     * • must not let password flow anywhere else
     * @param req the request
     * @param resp the response
     * @return the account
     */
    Acc login(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError, AccountIsLocked, EncryptionDontWork, AccountDoesNotExist;
}
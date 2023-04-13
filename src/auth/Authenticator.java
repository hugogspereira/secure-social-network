package auth;

import acc.Acc;
import crypto.EncryptDecryptUtils;
import exc.*;
import storage.DbAccount;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Authenticator class:
 *   Manages principal identities in the system.
 *   Identifies who is the principal invoking an operation.
 *   Provides a way to authenticate a principal.
 *
 * @author Hugo Pereira e Daniel Cavalheiro
 */
public class Authenticator implements Auth {

    /**
     * Authenticator constructor
     */
    public Authenticator() { }

    @Override
    public void createAccount(String name, String pwd1, String pwd2) throws NullParameterException, AccountAlreadyExists, PasswordsDontMatch, EncryptionDontWork {
        if(name == null || name.isEmpty() || pwd1 == null || pwd1.isEmpty() || pwd2 == null || pwd2.isEmpty()) {
            throw new NullParameterException();
        }
        else if (DbAccount.hasAccount(name)) {
            throw new AccountAlreadyExists();
        }
        else if (!pwd1.equals(pwd2)) {
            throw new PasswordsDontMatch();
        }

        DbAccount.createAccount(name,EncryptDecryptUtils.getInstance().encrypt(pwd1));
    }

    @Override
    public void deleteAccount(String name) throws AccountDoesNotExist, AccountIsLoggedIn, AccountIsNotLocked {
        Acc account = DbAccount.getAccount(name);
        if(account == null) {
            throw new AccountDoesNotExist();
        }
        else if(account.isLoggedIn()) {
            throw new AccountIsLoggedIn();
        }
        else if(!account.isLocked()) {
            throw new AccountIsNotLocked();
        }

        DbAccount.deleteAccount(name);
    }

    @Override
    public Acc getAccount(String name) throws AccountDoesNotExist {
        Acc account = DbAccount.getAccount(name);
        if(account == null) {
            throw new AccountDoesNotExist();
        }
        return account;
    }

    @Override
    public void changePwd(String name, String pwd1, String pwd2) throws NullParameterException, PasswordsDontMatch, EncryptionDontWork, AccountDoesNotExist {
        if(name == null || name.isEmpty() || pwd1 == null || pwd1.isEmpty() || pwd2 == null || pwd2.isEmpty()) {
            throw new NullParameterException();
        }
        else if (!pwd1.equals(pwd2)) {
            throw new PasswordsDontMatch();
        }
        Acc acc = DbAccount.getAccount(name);
        if(acc == null) {
            throw new AccountDoesNotExist();
        }
        acc.setPassword(EncryptDecryptUtils.getInstance().encrypt(pwd1));
        DbAccount.updateAccount(acc);
    }

    @Override
    public Acc authenticateUser(String name, String pwd) throws AccountDoesNotExist, AccountIsLocked, EncryptionDontWork, AuthenticationError {
        Acc account = DbAccount.getAccount(name);
        if(account == null) {
            throw new AccountDoesNotExist();
        }
        else if(account.isLocked()) {
            throw new AccountIsLocked();
        }
        else if(EncryptDecryptUtils.getInstance().encrypt(pwd).equals(account.getPassword())) {
            account.setLoggedIn(true);
            return account;
        }
        else {
            throw new AuthenticationError();
        }
    }

    @Override
    public void logout(Acc acc) {
        Acc account = DbAccount.getAccount(acc.getAccountName());
        account.setLoggedIn(false);
        DbAccount.updateAccount(account);
    }

    @Override
    public Acc checkAuthenticatedRequest(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError, AccountIsLocked, EncryptionDontWork, AccountDoesNotExist {
        // This is using session parameters.
        // TODO: Professor wants us to use JWT instead.
        String accountName = req.getParameter("accountName");
        String password = req.getParameter("password");

        // This is using JWT
        //  String jwt = req.getSession().getAttribute("JWT").toString();
        // Get the accountName and password from the JWT
        // refresh the JWT

        Acc account = DbAccount.getAccount(accountName);
        if(account == null) {
            throw new AuthenticationError();
        }
        return authenticateUser(accountName, password);
    }


}
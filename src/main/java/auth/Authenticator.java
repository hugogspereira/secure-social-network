package auth;

import acc.Acc;
import crypto.EncryptDecryptUtils;
import exc.*;
import storage.dbAccount;
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
        else if (dbAccount.hasAccount(name)) {
            throw new AccountAlreadyExists();
        }
        else if (!pwd1.equals(pwd2)) {
            throw new PasswordsDontMatch();
        }

        dbAccount.createAccount(name,EncryptDecryptUtils.getInstance().encrypt(pwd1));
    }

    @Override
    public void deleteAccount(String name) throws AccountDoesNotExist, AccountIsLoggedIn, AccountIsNotLocked {
        Acc account = dbAccount.getAccount(name);
        if(account == null) {
            throw new AccountDoesNotExist();
        }
        else if(account.isLoggedIn()) {
            throw new AccountIsLoggedIn();
        }
        else if(!account.isLocked()) {
            throw new AccountIsNotLocked();
        }

        dbAccount.deleteAccount(name);
    }

    @Override
    public Acc getAccount(String name) throws AccountDoesNotExist {
        Acc account = dbAccount.getAccount(name);
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
        Acc acc = dbAccount.getAccount(name);
        if(acc == null) {
            throw new AccountDoesNotExist();
        }
        acc.setPassword(EncryptDecryptUtils.getInstance().encrypt(pwd1));
        dbAccount.updateAccount(acc);
    }

    @Override
    public Acc login(String name, String pwd) throws AccountDoesNotExist, AccountIsLocked, EncryptionDontWork, AuthenticationError {
        Acc account = dbAccount.getAccount(name);
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
        Acc account = dbAccount.getAccount(acc.getAccountName());
        account.setLoggedIn(false);
        dbAccount.updateAccount(account);
    }

    @Override
    public Acc login(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError, AccountIsLocked, EncryptionDontWork, AccountDoesNotExist {
        // Not sure if it is done like this
        String accountName = req.getParameter("accountName");
        String password = req.getParameter("password");

        Acc account = dbAccount.getAccount(accountName);
        if(account == null) {
            throw new AuthenticationError();
        }
        return login(accountName, password);
    }


}
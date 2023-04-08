package auth;

import acc.Acc;
import acc.Account;
import java.util.HashMap;
import java.util.Map;
import crypto.EncryptDecryptUtils;
import exc.*;
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

    // TODO: change to database instead of hashmap
    private Map<String, Acc> accounts;

    /**
     * Authenticator constructor
     */
    public Authenticator() {
        this.accounts = new HashMap<>();
    }

    @Override
    public void createAccount(String name, String pwd1, String pwd2) throws NullParameterException, AccountAlreadyExists, PasswordsDontMatch, EncryptionDontWork {
        if(name == null || name.isEmpty() || pwd1 == null || pwd1.isEmpty() || pwd2 == null || pwd2.isEmpty()) {
            throw new NullParameterException();
        }
        else if (accounts.containsKey(name)) {
            throw new AccountAlreadyExists();
        }
        else if (!pwd1.equals(pwd2)) {
            throw new PasswordsDontMatch();
        }

        Acc acc = new Account(name, EncryptDecryptUtils.getInstance().encrypt(pwd1));
        // TODO: add account to database instead of hashmap
        accounts.put(name, acc);
    }

    @Override
    public void deleteAccount(String name) throws AccountDoesNotExist, AccountIsLoggedIn, AccountIsNotLocked {
        // TODO: change the get to a database query
        Acc account = accounts.get(name);
        if(account == null) {
            throw new AccountDoesNotExist();
        }
        else if(account.isLoggedIn()) {
            throw new AccountIsLoggedIn();
        }
        else if(!account.isLocked()) {
            throw new AccountIsNotLocked();
        }

        // TODO: remove account on database instead of hashmap
        accounts.remove(name);
    }

    @Override
    public Acc getAccount(String name) throws AccountDoesNotExist {
        // TODO: change the get to a database query
        Acc account = accounts.get(name);
        if(account == null) {
            throw new AccountDoesNotExist();
        }
        return account;
    }

    @Override
    public void changePwd(String name, String pwd1, String pwd2) throws NullParameterException, PasswordsDontMatch, EncryptionDontWork {
        if(name == null || name.isEmpty() || pwd1 == null || pwd1.isEmpty() || pwd2 == null || pwd2.isEmpty()) {
            throw new NullParameterException();
        }
        else if (!pwd1.equals(pwd2)) {
            throw new PasswordsDontMatch();
        }

        Acc acc = accounts.get(name);
        acc.setPassword(EncryptDecryptUtils.getInstance().encrypt(pwd1));
        // TODO: add account to database instead of hashmap
        accounts.replace(name, acc);
    }

    @Override
    public Acc login(String name, String pwd) throws AccountDoesNotExist, AccountIsLocked, EncryptionDontWork, AuthenticationError {
        // TODO: change the get to a database query
        Acc account = accounts.get(name);
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
        // TODO: change the set to a database query
        accounts.get(acc.getAccountName()).setLoggedIn(false);
    }

    @Override
    public Acc login(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError, AccountIsLocked, EncryptionDontWork, AccountDoesNotExist {
        // Not sure if it is done like this
        String accountName = req.getParameter("accountName");
        String password = req.getParameter("password");
        // TODO: change the get to a database query
        Acc account = accounts.get(accountName);
        if(account == null) {
            throw new AuthenticationError();
        }
        return login(accountName, password);
    }


}
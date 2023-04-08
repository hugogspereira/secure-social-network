package auth;

import acc.Acc;
import acc.Account;
import java.util.HashMap;
import java.util.Map;

import crypto.EncryptDecryptUtils;
import exc.*;

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

}
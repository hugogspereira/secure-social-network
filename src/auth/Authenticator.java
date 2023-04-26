package auth;

import acc.Acc;
import crypto.EncryptDecryptUtils;
import exc.*;
import jwt.JWTAccount;
import storage.DbAccount;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Authenticator class:
 *   Manages principal identities in the system.
 *   Identifies who is the principal invoking an operation.
 *   Provides a way to authenticate a principal.
 *
 * @author Hugo Pereira e Daniel Cavalheiro
 */
public class Authenticator implements Auth {

    private static Authenticator auth;

    /**
     * Authenticator constructor
     */
    private Authenticator() {}

    public static Authenticator getInstance() {
        if(auth == null) {
            auth = new Authenticator();
        }
        return auth;
    }

    @Override
    public void createAccount(String name, String pwd1, String pwd2) throws NullParameterException, AccountAlreadyExists, PasswordsDontMatch, EncryptionDontWork {
        if(name == null || name.isEmpty() || pwd1 == null || pwd1.isEmpty() || pwd2 == null || pwd2.isEmpty()) {
            throw new NullParameterException();
        }
        else if (DbAccount.getInstance().hasAccount(name)) {
            throw new AccountAlreadyExists();
        }
        else if (!pwd1.equals(pwd2)) {
            throw new PasswordsDontMatch();
        }

        DbAccount.getInstance().createAccount(name,EncryptDecryptUtils.getInstance().encrypt(pwd1));
    }

    @Override
    public void deleteAccount(String name) throws AccountDoesNotExist, AccountIsLoggedIn, AccountIsNotLocked {
        Acc account = DbAccount.getInstance().getAccount(name);
        if(account == null) {
            throw new AccountDoesNotExist();
        }
        else if(account.isLoggedIn()) {
            throw new AccountIsLoggedIn();
        }
        else if(!account.isLocked()) {
            throw new AccountIsNotLocked();
        }

        DbAccount.getInstance().deleteAccount(name);
    }

    @Override
    public Acc getAccount(String name) throws AccountDoesNotExist {
        Acc account = DbAccount.getInstance().getAccount(name);
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
        Acc acc = DbAccount.getInstance().getAccount(name);
        if(acc == null) {
            throw new AccountDoesNotExist();
        }
        acc.setPassword(EncryptDecryptUtils.getInstance().encrypt(pwd1));
        DbAccount.getInstance().updateAccount(acc);
    }

    @Override
    public Acc authenticateUser(String name, String pwd) throws AccountDoesNotExist, AccountIsLocked, EncryptionDontWork, AuthenticationError {
        Acc account = DbAccount.getInstance().getAccount(name);
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
        Acc account = DbAccount.getInstance().getAccount(acc.getAccountName());
        account.setLoggedIn(false);
        DbAccount.getInstance().updateAccount(account);
    }

    @Override
    public Acc checkAuthenticatedRequest(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationError, AccountIsLocked, EncryptionDontWork, AccountDoesNotExist {
        String jwt = req.getSession().getAttribute("JWT").toString();

        // Get the accountName and password from the JWT
        String accountName = (String) JWTAccount.getInstance().getClaim(jwt, "accountName");
        String password = (String) JWTAccount.getInstance().getClaim(jwt, "password");


        Acc account = DbAccount.getInstance().getAccount(accountName);
        if(account == null) {
            throw new AuthenticationError();
        }

        // refresh the JWT
        HttpSession session = req.getSession(true);
        session.setAttribute("JWT", JWTAccount.getInstance().createJWT(accountName, password));

        return account;
    }


}
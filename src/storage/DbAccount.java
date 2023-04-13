package storage;

import acc.Acc;
import acc.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbAccount {

    /**
     * Get a connection to the database
     * @return the connection
     */
    public static Connection getConnection() {
        try {
            // TODO: add the SQLite.db file to the resources folder
            // TODO: see if this works (probably the path is not correct)
            Class.forName("org.sqlite.JDBC");
            String sqliteConn = "jdbc:sqlite:src/main/resources/SQLite.db";

            return DriverManager.getConnection(sqliteConn);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    /**
     * Create a new account in the database
     * @param accountName the account name
     * @param password the account password
     */
    public static void createAccount(String accountName, String password) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Accounts (accountName, password, loggedIn, locked) VALUES (?, ?, ?, ?)");
            ps.setString(1, accountName);
            ps.setString(2, password);
            ps.setBoolean(3, false);
            ps.setBoolean(4, false);
            ps.executeUpdate();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * See if an account with a given name exist in the database, and retrieves it
     * @param accountName the account name
     */
    public static Acc getAccount(String accountName) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Accounts WHERE accountName = ?");
            ps.setString(1, accountName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Acc acc = new Account(rs.getString("accountName"), rs.getString("password"));
                acc.setLoggedIn(rs.getBoolean("loggedIn"));
                acc.setLocked(rs.getBoolean("locked"));

                return acc;
            }
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * See if an account with a given name exist in the database
     * @param accountName the account name
     */
    public static boolean hasAccount(String accountName) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Accounts WHERE accountName = ?");
            ps.setString(1, accountName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return true;
            }
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete an account from the database by account name
     * @param accountName the account name
     */
    public static void deleteAccount(String accountName) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Accounts WHERE accountName = ?");
            ps.setString(1, accountName);
            ps.executeUpdate();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an account in the database
     * @param acc the account
     */
    public static void updateAccount(Acc acc) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE Accounts SET password = ?, loggedIn = ?, locked = ? WHERE accountName = ?");
            ps.setString(1, acc.getPassword());
            ps.setBoolean(2, acc.isLoggedIn());
            ps.setBoolean(3, acc.isLocked());
            ps.setString(4, acc.getAccountName());
            ps.executeUpdate();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

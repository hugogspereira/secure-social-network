package storage;

import acc.Acc;
import acc.Account;
import java.sql.*;

public class DbAccount {

    private static final int FALSE = 0;
    private static final int TRUE = 1;

    /**
     * The instance of the DbAccount class
     */
    private static DbAccount instance;

    /**
     * Get the instance of the DbAccount class
     * @return the instance
     */
    public static DbAccount getInstance() {
        if (instance == null) {
            instance = new DbAccount();
        }
        return instance;
    }

    /**
     * The constructor of the DbAccount class
     */
    public DbAccount() {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS accounts (accountName VARCHAR(255), password VARCHAR(255), loggedIn INTEGER, locked INTEGER)");
            ps.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get a connection to the database
     * @return the connection
     */
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String sqliteConn = "jdbc:sqlite:SQLite.db";
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
    public void createAccount(String accountName, String password) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO accounts (accountName, password, loggedIn, locked) VALUES (?, ?, ?, ?)");
            ps.setString(1, accountName);
            ps.setString(2, password);
            ps.setInt(3, FALSE);
            ps.setInt(4, FALSE);
            ps.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * See if an account with a given name exist in the database, and retrieves it
     * @param accountName the account name
     */
    public Acc getAccount(String accountName) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts WHERE accountName = ?");
            ps.setString(1, accountName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Acc acc = new Account(rs.getString("accountName"), rs.getString("password"));
                acc.setLoggedIn(rs.getBoolean("loggedIn"));
                acc.setLocked(rs.getBoolean("locked"));
                conn.close();
                return acc;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * See if an account with a given name exist in the database
     * @param accountName the account name
     */
    public boolean hasAccount(String accountName) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts WHERE accountName = ?");
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
        finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Delete an account from the database by account name
     * @param accountName the account name
     */
    public void deleteAccount(String accountName) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM accounts WHERE accountName = ?");
            ps.setString(1, accountName);
            ps.executeUpdate();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update an account in the database
     * @param acc the account
     */
    public void updateAccount(Acc acc) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET password = ?, loggedIn = ?, locked = ? WHERE accountName = ?");
            ps.setString(1, acc.getPassword());
            if(acc.isLoggedIn()) {
                ps.setInt(2, TRUE);
            }
            else {
                ps.setInt(2, FALSE);
            }
            if(acc.isLocked()) {
                ps.setInt(3, TRUE);
            }
            else {
                ps.setInt(3, FALSE);
            }
            ps.setString(4, acc.getAccountName());
            ps.executeUpdate();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Good for debug
    public void viewRows() {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                System.out.println("Account Name: " + rs.getString("accountName") + " Password: " + rs.getString("password") + " Logged In: " + rs.getInt("loggedIn") + " Locked: " + rs.getInt("locked"));
            }
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

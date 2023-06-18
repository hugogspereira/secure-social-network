package storage;

import acc.Acc;
import acc.Account;
import accCtrl.Role;
import accCtrl.RoleClass;
import accCtrl.operations.Operation;
import accCtrl.resources.Resource;
import socialNetwork.SN;
import util.Util;

import java.sql.*;
import java.util.*;

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
            PreparedStatement ps1 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS accounts (accountName VARCHAR(255), password VARCHAR(255), loggedIn INTEGER, locked INTEGER)");
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS accountRoles (accountName VARCHAR(255), roleId VARCHAR(255))");
            ps2.executeUpdate();

            PreparedStatement ps3 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS rolePermissions (roleId VARCHAR(255), resource VARCHAR(255), operation VARCHAR(255))");
            ps3.executeUpdate();

            PreparedStatement ps4 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS roles (roleId VARCHAR(255))");
            ps4.executeUpdate();
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
            String sqliteConn = "jdbc:sqlite:../webapps/seg-soft/SQLite.db";
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

    public void logoutAccount(String accountName) throws Exception {
        SN.getInstance().closeConn();
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET loggedIn = ? WHERE accountName = ?");
            ps.setInt(1, FALSE);
            ps.setString(2, accountName);
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
     * Create a new role in the database
     * @param roleId the role identifier
     */
    public void newRole(String roleId) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO roles (roleId) VALUES (?)");
            ps.setString(1, roleId);
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
     * Set a role into an account in the database
     * @param accountName the account name
     * @param roleId the role identifier
     */
    public void setRole(String accountName, String roleId) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO accountRoles (accountName, roleId) VALUES (?, ?)");
            ps.setString(1, accountName);
            ps.setString(2, roleId);
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
     * Get roles of a given account in the database
     * @param accountName the account name
     */
    public List<Role> getRoles(String accountName) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT roleId FROM accountRoles WHERE accountName = ?");
            ps.setString(1, accountName);
            ResultSet rs = ps.executeQuery();

            List<Role> roles = new LinkedList<>();
            if(rs.next()) {
                Role role = new RoleClass(rs.getString("roleId"));
                roles.add(role);
            }
            return roles;
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
        return null;
    }

    /**
     * Insert in the database the new permissions, this is the operations that a role can do on a resource
     * @param role the role
     * @param res the resource
     * @param op the operation
     */
    public void grantPermission(Role role, Resource res, Operation op) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO rolePermissions (roleId, resource, operation) VALUES (?, ?, ?)");
            ps.setString(1, role.getRoleId());
            ps.setString(2, res.getResourceType());
            ps.setString(3, op.getOperationId());
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
     * Delete in the database the following permission, this is the operations that a role cannot do on a resource
     * @param role the role
     * @param res the resource
     * @param op the operation
     */
    public void revokePermission(Role role, Resource res, Operation op) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM rolePermissions WHERE roleId = ? AND resource = ? AND operation = ?");
            ps.setString(1, role.getRoleId());
            ps.setString(2, res.getResourceType());
            ps.setString(3, op.getOperationId());
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
     * Get in the database the permissions, this is the operations that a role can do on the resources
     * @param role the role
     */
    public List<String> getPermissions(Role role) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM rolePermissions WHERE roleId = ?");
            ps.setString(1, role.getRoleId());
            ResultSet rs = ps.executeQuery();

            List<String> list = new LinkedList<>();
            while(rs.next()) {
                String resourceType = rs.getString("resource");
                String operationId = rs.getString("operation");
                list.add(Util.getHash(Util.serializeToBytes(new String[]{resourceType, operationId})));
            }
            return list;
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
        return null;
    }

    /**
     * Check if a role has a permission to do an operation on a resource in the database
     * @param res the resource
     * @param op the operation
     */
    public boolean hasPermission(String roleId, Resource res, Operation op) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM rolePermissions WHERE roleId = ? AND resource = ? AND operation = ?");
            ps.setString(1, roleId);
            ps.setString(2, res.getResourceType());
            ps.setString(3, op.getOperationId());

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
               return true;
            }
            return false;
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

    private boolean checkIfLoggedInAccount(String accountName) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT loggedIn FROM accounts WHERE accountName = ?");
            ps.setString(1, accountName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return rs.getInt("loggedIn") == 1;
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

    private void lockAccount(String accountName) {
        auxLockUnlock(accountName, true);
    }

    private void rollbackLock(String accountName) {
        auxLockUnlock(accountName, false);
    }

    private void auxLockUnlock(String accountName, boolean lock) {
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET locked = ? WHERE accountName = ?");
            ps.setInt(1, lock ? TRUE : FALSE);
            ps.setString(2, accountName);
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

    public void lockAccDb(String accountName) {
        DbAccount db = DbAccount.getInstance();
        if(!db.checkIfLoggedInAccount(accountName)) { // if the account is not logged in
            db.lockAccount(accountName); // lock the account
            if(db.checkIfLoggedInAccount(accountName)) { // check again to see if someone logged in
                db.rollbackLock(accountName); // rollback the lock
            }
        }
    }
}

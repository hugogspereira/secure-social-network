package accCtrl;

import acc.Acc;
import accCtrl.operations.Operation;
import accCtrl.resources.Resource;
import exc.AccessControlError;
import storage.DbAccount;
import util.Util;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccessControllerClass implements AccessController {

    private static AccessController accCtrl;
    /*
     * Map to store the capabilities that were revoked while the system is running
     * and other to store the capabilities that were granted while the system is running
     * It is only stored the generic capabilities - the set does not become big
     */
    private static Set<String> revokedCapabilities, grantedCapabiltiies;

    /**
     * Access Controller constructor
     */
    private AccessControllerClass() {}

    public static AccessController getInstance() {
        if(accCtrl == null) {
            revokedCapabilities = new HashSet<>();
            grantedCapabiltiies = new HashSet<>();

            accCtrl = new AccessControllerClass();
        }
        return accCtrl;
    }

    @Override
    public Role newRole(String roleId) {
        DbAccount.getInstance().newRole(roleId);
        return new RoleClass(roleId);
    }

    @Override
    public void setRole(Acc user, Role role) {
        DbAccount.getInstance().setRole(user.getAccountName(), role.getRoleId());
    }

    @Override
    public List<Role> getRole(Acc user) {
        return DbAccount.getInstance().getRoles(user.getAccountName());
    }

    @Override
    public void grantPermission(Role role, Resource res, Operation op) throws Exception {
        DbAccount.getInstance().grantPermission(role, res, op);

        // Run time storing capabilities
        try {
            String cap = Util.getHash(Util.serializeToBytes(new String[]{res.getResourceType(), op.getOperationId()}));
            grantedCapabiltiies.add(cap);
            revokedCapabilities.remove(cap);
        }
        catch (NoSuchAlgorithmException e) {
            throw new Exception(e);
        }
    }

    @Override
    public void revokePermission(Role role, Resource res, Operation op) throws Exception {
        DbAccount.getInstance().revokePermission(role, res, op);

        // Run time storing capabilities
        try {
            String cap = Util.getHash(Util.serializeToBytes(new String[]{res.getResourceType(), op.getOperationId()}));
            revokedCapabilities.add(cap);
            grantedCapabiltiies.remove(cap);
        }
        catch (NoSuchAlgorithmException e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<String> makeKey(Role role) {
        return DbAccount.getInstance().getPermissions(role);
    }

    @Override
    public void checkPermission(List<String> capabilities, Resource res, Operation op, DBcheck c) throws Exception {
        String genericCap = Util.getHash(Util.serializeToBytes(new Object[]{res.getResourceType(), op.getOperationId()}));
        // Check if (while running the system) there was a revoke of a permission that possibly can compromise the system access control
        if(!revokedCapabilities.contains(genericCap)) {
            String specificCap = Util.getHash(Util.serializeToBytes(new Object[]{res.getResourceId(), op.getOperationId()}));
            if(capabilities.stream().anyMatch((s) -> s.equals(specificCap))) {
                return;
            }

            // If the session as the capability or if it was granted in the running time
            if(capabilities.stream().anyMatch((s) -> s.equals(genericCap)) || grantedCapabiltiies.contains(genericCap)) {
                if(c.checkDB(specificCap)){
                    return;
                }
            }
        }
        throw new AccessControlError("User does not have permission to perform this operation on this resource");
    }

}
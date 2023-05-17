package accCtrl;

import acc.Acc;
import storage.DbAccount;
import java.util.List;

public class AccessControllerClass implements AccessController {

    public AccessControllerClass() {
    }

    @Override
    public Role newRole(String roleId) {
        // put in Db
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
    public void grantPermission(Role role, Resource res, Operation op) {
        // connect to db
        // insert row on the table of permissions with the roleId   (roleId, res, op)
    }

    @Override
    public void revokePermission(Role role, Resource res, Operation op) {
        // connect to db
        // update row on the table of permissions with the roleId   (roleId, res, op)
    }

    @Override
    public Capability makeKey(Role role) {
        // TODO
        return new CapabilityClass(role);
    }

    @Override
    public void checkPermission(Capability key, Resource res, Operation op) {
        // TODO
    }

}

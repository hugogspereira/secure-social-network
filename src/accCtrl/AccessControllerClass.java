package accCtrl;

import acc.Acc;
import java.util.List;

public class AccessControllerClass implements AccessController {

    public AccessControllerClass() {
        // TODO - connect to db
    }

    @Override
    public Role newRole(String roleId) {
        return new RoleClass(roleId);
    }

    @Override
    public void setRole(Acc user, Role role) {
        // connect to db
        // update the table of accountRoles with the accountId   (accountId, roleId)
    }

    @Override
    public List<Role> getRole(Acc user) {
        // connect to db
        // get rows of the table of accountRoles with the accountId   (accountId, roleId)
        return null;
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
        return new CapabilityClass(role);
    }

    @Override
    public void checkPermission(Capability key, Resource res, Operation op) {
        // TODO
    }

}

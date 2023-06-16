package accCtrl;

import acc.Acc;
import accCtrl.operations.Operation;
import accCtrl.resources.Resource;
import exc.AccessControlError;
import storage.DbAccount;
import util.Util;

import java.util.List;

public class AccessControllerClass implements AccessController {

    private static AccessController accCtrl;

    /**
     * Access Controller constructor
     */
    private AccessControllerClass() {}

    public static AccessController getInstance() {
        if(accCtrl == null) {
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
    public void grantPermission(Role role, Resource res, Operation op) {
        DbAccount.getInstance().grantPermission(role, res, op);
    }

    @Override
    public void revokePermission(Role role, Resource res, Operation op) {
        DbAccount.getInstance().revokePermission(role, res, op);
    }

    @Override
    public List<String> makeKey(Role role) {

        return DbAccount.getInstance().getPermissions(role);
    }

    @Override
    public void checkPermission(List<String> capabilities, Resource res, Operation op, DBcheck c) throws Exception {
        /*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * TODO: Nota: Temos de ter a certeza que a capability tem sempre o valor mais atualizado
         * Quando é q a capability é atualizada? Sempre que é feito um grant ou revoke
         * Quando é que metemos a capability atualizada na sessão? (O possivel ramo else no inner loop deste método)
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        String especificCap = Util.getHash(Util.serializeToBytes(new Object[]{res.getResourceId(), op.getOperationId()}));
        if(capabilities.stream().anyMatch((s) -> s.equals(especificCap))) {
            return;
        }

        String genericCap = Util.getHash(Util.serializeToBytes(new Object[]{res.getResourceType(), op.getOperationId()}));
        if(capabilities.stream().anyMatch((s) -> s.equals(genericCap))) {
            if(c.checkDB(especificCap)){
                return;
            }
        }

        throw new AccessControlError("User does not have permission to perform this operation on this resource");
    }

}

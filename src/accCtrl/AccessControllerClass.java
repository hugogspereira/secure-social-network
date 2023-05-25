package accCtrl;

import acc.Acc;
import accCtrl.operations.Operation;
import accCtrl.resources.Resource;
import storage.DbAccount;
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
    public Capability makeKey(Role role) {
        // TODO
        return new CapabilityClass(role);
    }

    @Override
    public void checkPermission(Capability key, Resource res, Operation op) {
        // TODO

        // Criar uma capability a partir do res e do op e ver se é "igual" à capability q foi passada como argumento
            // NOTA: n é ser igual, é conter essa permissao
        // Se for igual, então o user tem permissão
        // Se não for igual, então temos de ir à DB ver se tem permissão
        // Se nao tiver, então nao tem permissão
    }

}

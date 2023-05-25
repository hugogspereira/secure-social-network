package accCtrl;

import acc.Acc;
import accCtrl.operations.Operation;
import accCtrl.resources.Resource;

import java.util.List;

public interface AccessController {

    Role newRole(String roleId);

    void setRole(Acc user, Role role);

    List<Role> getRole(Acc user);

    void grantPermission(Role role, Resource res, Operation op);

    void revokePermission(Role role, Resource res, Operation op);

    Capability makeKey(Role role);

    void checkPermission(Capability key, Resource res, Operation op);

}

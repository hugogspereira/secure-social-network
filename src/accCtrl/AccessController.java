package accCtrl;

import acc.Acc;
import accCtrl.operations.Operation;
import accCtrl.resources.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface AccessController {

    Role newRole(String roleId);

    void setRole(Acc user, Role role);

    List<Role> getRole(Acc user);

    void checkIfNeedsToRefreshCapabilities(String username, HttpSession session);

    void grantPermission(Role role, Resource res, Operation op);

    void revokePermission(Role role, Resource res, Operation op);

    List<String> makeKey(Role role);

    void checkPermission(List<String> key, Resource res, Operation op, DBcheck check) throws Exception;

}

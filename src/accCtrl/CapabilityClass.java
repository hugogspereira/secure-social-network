package accCtrl;

import accCtrl.operations.Operation;
import accCtrl.resources.Resource;
import java.util.List;
import java.util.Map;

public class CapabilityClass implements Capability {

    private Role role;
    private Map<String, List<String>> permissions;

    public CapabilityClass(Role role, Map<String, List<String>> permissions) {
        this.role = role;
        this.permissions = permissions;
    }

    /**
     * Get the role id
     * @return role id
     */
    public String getRoleId() {
        return role.getRoleId();
    }

    /**
     * Check if the capability has permission to perform the operation on the resource
     * @param res resource
     * @param op operation
     * @return true if the capability has permission to perform the operation on the resource
     */
    public boolean hasPermission(Resource res, Operation op) {
        if(permissions == null) {
            return false;
        }
        return permissions.get(res.getResourceId()).contains(op.getOperationId());
    }

}

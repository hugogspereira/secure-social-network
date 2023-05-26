package accCtrl;

import accCtrl.operations.Operation;
import accCtrl.resources.Resource;

public interface Capability {

    public String getRoleId();
    public boolean hasPermission(Resource res, Operation op);

}

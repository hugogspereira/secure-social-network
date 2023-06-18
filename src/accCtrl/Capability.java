package accCtrl;

import accCtrl.operations.Operation;
import accCtrl.resources.Resource;

public interface Capability {

     String getRoleId();
     boolean hasPermission(Resource res, Operation op);

}

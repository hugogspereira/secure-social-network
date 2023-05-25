package accCtrl;

import accCtrl.operations.Operation;
import accCtrl.resources.Resource;

public interface Capability {

    public String getRoleId();
    public boolean hasPermission(Resource res, Operation op);

    // TODO: Do not know if the methods bellow are really needed or not, lets wait to see
    public byte[] serializeToBytes();
    public Capability deserializeFromBytes(byte[] bytes);
}

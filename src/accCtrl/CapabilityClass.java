package accCtrl;

import accCtrl.operations.Operation;
import accCtrl.resources.Resource;

import java.io.*;
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


    // TODO: Do not know if the methods bellow are really needed or not, lets wait to see
    public byte[] serializeToBytes() {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteOut)) {
            objectOut.writeObject(this);
            return byteOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public CapabilityClass deserializeFromBytes(byte[] bytes) {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
             ObjectInputStream objectIn = new ObjectInputStream(byteIn)) {
            return (CapabilityClass) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

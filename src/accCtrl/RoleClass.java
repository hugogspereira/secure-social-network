package accCtrl;

public class RoleClass implements Role {

    private String roleId;

    public RoleClass(String roleId) {
        this.roleId = roleId;
    }

    public RoleClass(RoleValues role) {
        this.roleId = role.getRoleName();
    }

    @Override
    public String getRoleId() {
        return roleId;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RoleClass other = (RoleClass) obj;
        return roleId.equals(other.roleId);
    }

    @Override
    public int hashCode() {
        return roleId.hashCode();
    }

}

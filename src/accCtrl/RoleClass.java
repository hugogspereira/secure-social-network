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

}

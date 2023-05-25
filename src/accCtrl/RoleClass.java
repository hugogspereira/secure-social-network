package accCtrl;

public class RoleClass implements Role {

    private String roleId;

    public RoleClass(String roleId) {
        this.roleId = roleId;
    }
    @Override
    public String getRoleId() {
        return roleId;
    }

}

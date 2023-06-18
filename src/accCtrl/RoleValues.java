package accCtrl;

public enum RoleValues {
    AUTHOR("author"),
    ADMIN("admin");

    private final String roleName;

    RoleValues(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}

package exc;

public class NoRoleError extends Exception {

    public NoRoleError() {
        super("No role found");
    }

    public NoRoleError(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
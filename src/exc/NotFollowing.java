package exc;

public class NotFollowing extends Exception {

    public NotFollowing() {
        super("It is not following this user");
    }

    public NotFollowing(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
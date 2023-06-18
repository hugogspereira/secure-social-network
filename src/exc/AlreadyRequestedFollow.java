package exc;

public class AlreadyRequestedFollow extends Exception {

    public AlreadyRequestedFollow() {
        super("Already tried to follow this user");
    }

    public AlreadyRequestedFollow(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
package exc;

public class NotAbleToAccept extends Exception {

    public NotAbleToAccept() {
        super("The following state is already accepted or the invite was not sent.");
    }

    public NotAbleToAccept(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}
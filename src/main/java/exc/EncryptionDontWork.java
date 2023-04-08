package exc;

/**
 * EncryptionDontWork exception
 * @author Hugo Pereira e Daniel Cavalheiro
 */
public class EncryptionDontWork extends Exception {

    public EncryptionDontWork() {
        super("encryption/decryption did not work properly");
    }

    public EncryptionDontWork(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }

}

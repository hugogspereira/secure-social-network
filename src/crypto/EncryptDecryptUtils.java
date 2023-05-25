package crypto;

import exc.EncryptionDontWork;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * EncryptDecryptUtils class - Provides methods to encrypt and decrypt data.
 * @author Prof Luis Caires
 */
public class EncryptDecryptUtils {

    /**
     * ALGO - Algorithm used to encrypt and decrypt data.
     */
    private static final String ALGO = "AES";
    /**
     * keyValue - Key used to encrypt and decrypt data.
     */
    private static final byte[] keyValue = new byte[] { 'F', 'C', 'T', '/', 'U', 'N', 'L', 'r', 'o', 'c', 'k','s', '!', '!', 'd', 'i' };

    /**
     * key - Key used to encrypt and decrypt data.
     */
    private Key key;

    /**
     * instance - Singleton instance.
     */
    private static EncryptDecryptUtils instance;

    /**
     * EncryptDecryptUtils constructor
     */
    public EncryptDecryptUtils() {
        key = new SecretKeySpec(keyValue, ALGO);
    }

    /**
     * getInstance - Returns the singleton instance.
     * @return the singleton instance
     */
    public static EncryptDecryptUtils getInstance() {
        if(instance == null) {
            instance = new EncryptDecryptUtils();
        }
        return instance;
    }

    /**
     * getKey - Returns the key used to encrypt and decrypt data.
     * @return the key
     */
    private Key getKey() {
        return key;
    }

    /**
     * encrypt - Encrypts data.
     * @param decryptedData the data to be encrypted
     * @return the encrypted data
     * @throws EncryptionDontWork if the encryption fails
     */
    public String encrypt(String decryptedData) throws EncryptionDontWork {
        try {
            return java.util.Base64.getEncoder().encodeToString(encryptToBytes(decryptedData));
        } catch (Exception e) {
            throw new EncryptionDontWork();
        }
    }

    /**
     * encrypt - Encrypts data.
     * @param decryptedData the data to be encrypted
     * @return the encrypted data
     * @throws EncryptionDontWork if the encrypted fails
     */
    public byte[] encryptToBytes(String decryptedData) throws EncryptionDontWork {
        try {
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, instance.getKey());
            return c.doFinal(decryptedData.getBytes());
        } catch (Exception e) {
            throw new EncryptionDontWork();
        }
    }

    /**
     * decrypt - Decrypts data.
     * @param encryptedData the data to be decrypted
     * @return the decrypted data
     * @throws EncryptionDontWork if the decryption fails
     */
    public String decrypt(String encryptedData) throws EncryptionDontWork {
        try {
            return new String(decryptToBytes(encryptedData));
        } catch (Exception e) {
            throw new EncryptionDontWork();
        }
    }

    /**
     * decrypt - Decrypts data.
     *
     * @param encryptedData the data to be decrypted
     * @return the decrypted data
     * @throws EncryptionDontWork if the decryption fails
     */
    public byte[] decryptToBytes(String encryptedData) throws EncryptionDontWork {
        try {
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, instance.getKey());
            byte[] decodedValue = java.util.Base64.getDecoder().decode(encryptedData);
            return c.doFinal(decodedValue);
        } catch (Exception e) {
            throw new EncryptionDontWork();
        }
    }

}

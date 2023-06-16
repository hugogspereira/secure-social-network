package util;
import accCtrl.Capability;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Util {

    public static String getHash(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes);
        return new String(messageDigest.digest());
    }

    public static byte[] serializeToBytes(Object[] objects) {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteOut)) {
            Arrays.stream(objects).sequential().forEach((o -> {
                try {
                    objectOut.writeObject(o);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
            return byteOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

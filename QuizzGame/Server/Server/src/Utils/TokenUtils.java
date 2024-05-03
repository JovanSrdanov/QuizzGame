package Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class TokenUtils {

    private static final String SECRET_KEY = "super_secret_key";
    private static final long TIME = 1 * 60 * 1000;

    public static String encryptUsernameAndDate(String username) throws Exception {
        String data = username + "|" + new Date().toString();
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static boolean isValidDecryption(String encryptedData) {
        try {
            Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            String decryptedString = new String(decryptedData);
            String[] parts = decryptedString.split("\\|");
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date creationDate = dateFormat.parse(parts[1]);
            Date currentDate = new Date();
            return (currentDate.getTime() - creationDate.getTime()) <= TIME;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromEncryptedData(String encryptedData) throws Exception {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        String decryptedString = new String(decryptedData);
        String[] parts = decryptedString.split("\\|");
        return parts[0];
    }

}

package encription;

import repository.UserRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Encription {

    private static final SecretKey SymmetricKey = createAESKey();
    private static final byte[] InitializationVector = createInitializationVector();
    private static final String AES = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";

    private static SecretKey createAESKey() {
        try {
            SecureRandom secureRandom = new SecureRandom("RSZEOS2024".getBytes());
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(128, secureRandom);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static byte[] createInitializationVector() {
        byte[] initializationVector = new byte[16];
        for (int i = 0; i < 16; i++) {
            initializationVector[i] = (byte) (i + 1);
        }
        return initializationVector;
    }

    public byte[] do_AESEncryption(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Encription.InitializationVector);
        cipher.init(Cipher.ENCRYPT_MODE, Encription.SymmetricKey, ivParameterSpec);
        return cipher.doFinal(plainText.getBytes());
    }

    public String do_AESDecryption(byte[] cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Encription.InitializationVector);
        cipher.init(Cipher.DECRYPT_MODE, Encription.SymmetricKey, ivParameterSpec);
        byte[] result = cipher.doFinal(cipherText);
        return new String(result);
    }

}

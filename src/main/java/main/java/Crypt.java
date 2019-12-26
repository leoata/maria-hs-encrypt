package main.java;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Crypt {

    private int iterations;

    //Recommended "PBKDF2WithHmacSHA256"
    private String encryptionAlgorithm;

    public Crypt(int iterations, String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.iterations = iterations;
    }

    public byte[] generateHash(String input, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(input.toCharArray(), salt, iterations, 256);
        return SecretKeyFactory.getInstance(encryptionAlgorithm).generateSecret(spec).getEncoded();
    }


    public byte[] generateSalt() {
        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public boolean isMatch(String inputString, byte[] salt, byte[] comparingHash) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return Arrays.equals(generateHash(inputString, salt), comparingHash);
    }

    public int getIterations() {
        return iterations;
    }
}

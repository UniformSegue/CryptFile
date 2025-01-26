package fr.uniform;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class EncryptorAesGcmPasswordFile {

    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;


    public static byte[] encrypt(byte[] pText, String password) throws Exception {


        byte[] salt = fr.uniform.Crypt.getRandomNonce(SALT_LENGTH_BYTE);


        byte[] iv = fr.uniform.Crypt.getRandomNonce(IV_LENGTH_BYTE);


        SecretKey aesKeyFromPassword = fr.uniform.Crypt.getAESKeyFromPassword(password.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);


        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] cipherText = cipher.doFinal(pText);


        byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();

        return cipherTextWithIvSalt;

    }


    static byte[] decrypt(byte[] cText, String password) throws Exception {

        byte[] decode = cText;

        ByteBuffer bb = ByteBuffer.wrap(decode);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);

        byte[] salt = new byte[SALT_LENGTH_BYTE];
        bb.get(salt);

        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        SecretKey aesKeyFromPassword = fr.uniform.Crypt.getAESKeyFromPassword(password.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] plainText = cipher.doFinal(cipherText);

        return plainText;

    }


    public static void encryptFile(String fromFile, String toFile, String password) throws Exception {

        // read a normal txt file
        byte[] fileContent = Files.readAllBytes(Paths.get(fromFile));

        // encrypt with a password
        byte[] encryptedText = EncryptorAesGcmPasswordFile.encrypt(fileContent, password);

        // save a file
        Path path = Paths.get(toFile);

        Files.write(path, encryptedText);

    }

    public static void decryptFile(String fromEncryptedFile,String toFile, String password) throws Exception {

        // read a file
        byte[] fileContent = Files.readAllBytes(Paths.get(fromEncryptedFile));

        byte[] encryptedText = EncryptorAesGcmPasswordFile.decrypt(fileContent, password);

        Path path = Paths.get(toFile);

        Files.write(path, encryptedText);
    }
}
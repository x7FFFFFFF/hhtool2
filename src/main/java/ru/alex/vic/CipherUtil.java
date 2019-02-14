package ru.alex.vic;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Scanner;

@Singleton
public class CipherUtil {

    public static final String UTF_8 = "UTF-8";
    public static final String ENCRYPT = "--encrypt";
    public static final String DECRYPT = "--decrypt";
    private final char[] key;

    @Inject
    public CipherUtil(@Named("key.file") String fileName) {
        try (final Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream(fileName), UTF_8)) {
            key = scanner.nextLine().toCharArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Cipher getCipher(final char[] secret, final int mode)
            throws GeneralSecurityException {
        final byte[] solt = {(byte) 0xB9, (byte) 0x1B, (byte) 0xC0, (byte) 0xA2,
                (byte) 0x41, (byte) 0x45, (byte) 0xE1, (byte) 0x11};
        final int iter = 19;
        final String alg = "PBEWithMD5AndDES"; //$NON-NLS-1$
        SecretKey key = SecretKeyFactory.getInstance(alg)
                .generateSecret(new PBEKeySpec(secret, solt, iter));
        Cipher result = Cipher.getInstance(key.getAlgorithm());
        result.init(mode, key, new PBEParameterSpec(solt, iter));
        return result;
    }

    private byte[] encrypt(byte[] data) {
        try {
            return getCipher(key, Cipher.ENCRYPT_MODE).doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    byte[] decrypt(byte[] data) {
        try {
            return getCipher(key, Cipher.DECRYPT_MODE).doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(final String data) {
        try {
            return Base64.getEncoder().encodeToString(encrypt(data.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(final String data) {
        final byte[] decode = Base64.getDecoder().decode(data);
        try {
            return new String(decrypt(decode), UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        String method, file, value;
        if (args.length != 3) {
            System.out.println("Usage:");
            System.out.println("СipherUtil [--decrypt|--encrypt]  <key file> <value>");
            try (Scanner reader = new Scanner(System.in)) {
                System.out.println("Enter method. 'd' - decrypt, 'e' -encrypt:");
                method = getMethod(reader.nextLine());
                System.out.println("method = " + method);
                System.out.println("Enter key file. Default - 'key.sec':");
                file = getFile(reader.nextLine(), "key.sec");
                System.out.println("file = " + file);
                System.out.println("Enter token value:");
                value = reader.nextLine();
                System.out.println("value = " + value);
            }
        } else {
            method = args[0];
            file = args[1];
            value = args[2];
        }
        System.out.println("Result:");
        System.out.println(getResult(method, file, value));


    }

    private static String getResult(String method, String file, String value) {
        CipherUtil util = new CipherUtil(file);
        switch (method) {
            case ENCRYPT:
                return util.encrypt(value);
            case DECRYPT:
                return util.decrypt(value);
            default:
                throw new IllegalArgumentException(method);
        }
    }

    private static String getFile(String name, String def) {
        return (name == null || name.isEmpty()) ? def : name;
    }

    private static String getMethod(String name) {
        switch (name) {
            case "d":
                return DECRYPT;
            case "e":
                return ENCRYPT;
            default:
                throw new IllegalArgumentException(name);
        }
    }


}

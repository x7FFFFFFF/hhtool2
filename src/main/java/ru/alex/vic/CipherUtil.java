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
        if (args.length != 3) {
            System.out.println("Usage:");
            System.out.println("СipherUtil [--decrypt|--encrypt]  <key file> <value>");
            return;
        }
        final String file = args[1];
        final String value = args[2];
        CipherUtil util = new CipherUtil(file);
        switch (args[0]) {
            case "--encrypt":
                System.out.println(util.encrypt(value));
                break;
            case "--decrypt":
                System.out.println(util.decrypt(value));
                break;
            default:
                throw new IllegalArgumentException(args[0]);
        }


    }


}

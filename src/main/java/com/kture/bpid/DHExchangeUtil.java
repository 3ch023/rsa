package com.kture.bpid;

import javax.crypto.*;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Mariia_Lukianets on 01.04.14.
 */
public class DHExchangeUtil {

    //private DHParameterSpec dhParameterSpec;

    private KeyAgreement aliceKeyAgree;
    private KeyAgreement bobKeyAgree;

    KeyPair aliceKpair;
    KeyPair bobKpair;

    private byte[] aliceSharedSecret;
    private byte[] bobSharedSecret;

    public DHExchangeUtil() {}

    public DHParameterSpec initDHParams() throws NoSuchAlgorithmException, InvalidParameterSpecException {
        //System.out.println("Using SKIP Diffie-Hellman parameters");
        return new DHParameterSpec(skip1024Modulus, skip1024Base);

//        System.out.println("Creating Diffie-Hellman parameters (takes VERY long) ...");
//        AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
//        paramGen.init(512);
//        AlgorithmParameters params = paramGen.generateParameters();
//        return  (DHParameterSpec)params.getParameterSpec(DHParameterSpec.class);
    }

    public KeyPair getAliceKpair (DHParameterSpec dhParamSpec) throws NoSuchAlgorithmException, InvalidKeyException,
                                                                        InvalidAlgorithmParameterException {
        //Alice creates her own DH key pair, using the DH parameters
        System.out.println("ALICE: Generate DH keypair ...");
        KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
        aliceKpairGen.initialize(dhParamSpec);
        KeyPair aliceKpair = aliceKpairGen.generateKeyPair();

        // Alice creates and initializes her DH KeyAgreement object
        System.out.println("ALICE: Initialization ...");
        aliceKeyAgree = KeyAgreement.getInstance("DH");
        aliceKeyAgree.init(aliceKpair.getPrivate());

        this.aliceKpair = aliceKpair;
        return  aliceKpair;
    }

    private DHParameterSpec getDHParamsFromKey(byte[] aPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Instantiating a DH public key from the encoded key material.
        KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(aPublicKey);
        PublicKey alicePubKey = bobKeyFac.generatePublic(x509KeySpec);

        // get DH parameters associated with public key.
        return  ((DHPublicKey)alicePubKey).getParams();
    }

    public KeyPair getBobKpair(DHParameterSpec dhParamSpec) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("BOB: Generate DH keypair ...");
        KeyPairGenerator bobKpairGen = KeyPairGenerator.getInstance("DH");
        bobKpairGen.initialize(dhParamSpec);
        KeyPair bobKpair = bobKpairGen.generateKeyPair();

        // Bob creates and initializes his DH KeyAgreement object
        System.out.println("BOB: Initialization ...");
        bobKeyAgree = KeyAgreement.getInstance("DH");
        bobKeyAgree.init(bobKpair.getPrivate());
        this.bobKpair = bobKpair;
        return bobKpair;
    }

    public void genSecret() throws Exception {
        //Instantiate a DH public key from encoded key material.
//        KeyFactory aliceKeyFac = KeyFactory.getInstance("DH");
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
//        PublicKey bobPubKey = aliceKeyFac.generatePublic(x509KeySpec);
        System.out.println("ALICE: Execute PHASE1 ...");
        aliceKeyAgree.doPhase(this.bobKpair.getPublic(), true);

        System.out.println("BOB: Execute PHASE1 ...");
        bobKeyAgree.doPhase(this.aliceKpair.getPublic(), true);

        byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();
        int aliceLen = aliceSharedSecret.length;

        byte[] bobSharedSecret = new byte[aliceLen];
        int bobLen = bobKeyAgree.generateSecret(bobSharedSecret, 0);

        System.out.println("Alice secret: " +
                toHexString(aliceSharedSecret));
        System.out.println("Bob secret: " +
                toHexString(bobSharedSecret));

        if (!java.util.Arrays.equals(aliceSharedSecret, bobSharedSecret))
            throw new Exception("Shared secrets differ");
        System.out.println("Shared secrets are the same");
    }

    public byte[] bobChiperEncrypt(String text) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        System.out.println("BOB: encrypting [" + text + "] ...");
        bobKeyAgree.doPhase(this.aliceKpair.getPublic(), true);
        SecretKey bobDesKey = bobKeyAgree.generateSecret("DES");

        //Bob encrypts, using DES in ECB mode
        Cipher bobCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        bobCipher.init(Cipher.ENCRYPT_MODE, bobDesKey);

        byte[] cleartext = text.getBytes();
        byte[] ciphertext = bobCipher.doFinal(cleartext);

        System.out.println("BOB: encrypted [" + new String(ciphertext) + "] ...");

        return ciphertext;
    }

    public byte[] aliceChiperEncrypt(String text) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        System.out.println("ALICE: encrypting [" + text + "] ...");
        aliceKeyAgree.doPhase(this.bobKpair.getPublic(), true);
        SecretKey aliceDesKey = aliceKeyAgree.generateSecret("DES");

        //Bob encrypts, using DES in ECB mode
        Cipher aliceCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        aliceCipher.init(Cipher.ENCRYPT_MODE, aliceDesKey);

        byte[] cleartext = text.getBytes();
        byte[] ciphertext = aliceCipher.doFinal(cleartext);
        System.out.println("ALICE: encrypted [" + new String(ciphertext) + "] ...");
        return ciphertext;
    }

    public String aliceChiperDecrypt(byte[] ciphertext) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        System.out.println("ALICE: decrypting [" + new String(ciphertext) + "] ...");
        aliceKeyAgree.doPhase(this.bobKpair.getPublic(), true);
        SecretKey aliceDesKey = aliceKeyAgree.generateSecret("DES");

        //Alice decrypts, using DES in ECB mode
        Cipher aliceCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        aliceCipher.init(Cipher.DECRYPT_MODE, aliceDesKey);
        byte[] recovered = aliceCipher.doFinal(ciphertext);
        System.out.println("ALICE: decrypted [" + new String(recovered, "UTF-8") + "] ...");
        return new String(recovered, "UTF-8");
    }

    public String bobChiperDecrypt(byte[] ciphertext) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        System.out.println("BOB: decrypting [" + new String(ciphertext) + "] ...");
        bobKeyAgree.doPhase(this.aliceKpair.getPublic(), true);
        SecretKey bobDesKey = bobKeyAgree.generateSecret("DES");

        //Alice decrypts, using DES in ECB mode
        Cipher bobCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        bobCipher.init(Cipher.DECRYPT_MODE, bobDesKey);
        byte[] recovered = bobCipher.doFinal(ciphertext);
        System.out.println("BOB: decrypted [" + new String(recovered, "UTF-8") + "] ...");
        return new String(recovered, "UTF-8");
    }

    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
     * Converts a byte array to hex string
     */
    private String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();

        int len = block.length;

        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len-1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    /*
     * Prints the usage of this test.
     */
    private void usage() {
        System.err.print("DHKeyAgreement usage: ");
        System.err.println("[-gen]");
    }

    // The 1024 bit Diffie-Hellman modulus values used by SKIP
    private static final byte skip1024ModulusBytes[] = {
            (byte)0xF4, (byte)0x88, (byte)0xFD, (byte)0x58,
            (byte)0x4E, (byte)0x49, (byte)0xDB, (byte)0xCD,
            (byte)0x20, (byte)0xB4, (byte)0x9D, (byte)0xE4,
            (byte)0x91, (byte)0x07, (byte)0x36, (byte)0x6B,
            (byte)0x33, (byte)0x6C, (byte)0x38, (byte)0x0D,
            (byte)0x45, (byte)0x1D, (byte)0x0F, (byte)0x7C,
            (byte)0x88, (byte)0xB3, (byte)0x1C, (byte)0x7C,
            (byte)0x5B, (byte)0x2D, (byte)0x8E, (byte)0xF6,
            (byte)0xF3, (byte)0xC9, (byte)0x23, (byte)0xC0,
            (byte)0x43, (byte)0xF0, (byte)0xA5, (byte)0x5B,
            (byte)0x18, (byte)0x8D, (byte)0x8E, (byte)0xBB,
            (byte)0x55, (byte)0x8C, (byte)0xB8, (byte)0x5D,
            (byte)0x38, (byte)0xD3, (byte)0x34, (byte)0xFD,
            (byte)0x7C, (byte)0x17, (byte)0x57, (byte)0x43,
            (byte)0xA3, (byte)0x1D, (byte)0x18, (byte)0x6C,
            (byte)0xDE, (byte)0x33, (byte)0x21, (byte)0x2C,
            (byte)0xB5, (byte)0x2A, (byte)0xFF, (byte)0x3C,
            (byte)0xE1, (byte)0xB1, (byte)0x29, (byte)0x40,
            (byte)0x18, (byte)0x11, (byte)0x8D, (byte)0x7C,
            (byte)0x84, (byte)0xA7, (byte)0x0A, (byte)0x72,
            (byte)0xD6, (byte)0x86, (byte)0xC4, (byte)0x03,
            (byte)0x19, (byte)0xC8, (byte)0x07, (byte)0x29,
            (byte)0x7A, (byte)0xCA, (byte)0x95, (byte)0x0C,
            (byte)0xD9, (byte)0x96, (byte)0x9F, (byte)0xAB,
            (byte)0xD0, (byte)0x0A, (byte)0x50, (byte)0x9B,
            (byte)0x02, (byte)0x46, (byte)0xD3, (byte)0x08,
            (byte)0x3D, (byte)0x66, (byte)0xA4, (byte)0x5D,
            (byte)0x41, (byte)0x9F, (byte)0x9C, (byte)0x7C,
            (byte)0xBD, (byte)0x89, (byte)0x4B, (byte)0x22,
            (byte)0x19, (byte)0x26, (byte)0xBA, (byte)0xAB,
            (byte)0xA2, (byte)0x5E, (byte)0xC3, (byte)0x55,
            (byte)0xE9, (byte)0x2F, (byte)0x78, (byte)0xC7
    };

    // The SKIP 1024 bit modulus
    private static final BigInteger skip1024Modulus
            = new BigInteger(1, skip1024ModulusBytes);

    // The base used with the SKIP 1024 bit modulus
    private static final BigInteger skip1024Base = BigInteger.valueOf(2);

    public KeyPair getAliceKpair() {
        return aliceKpair;
    }

    public KeyPair getBobKpair() {
        return bobKpair;
    }
}

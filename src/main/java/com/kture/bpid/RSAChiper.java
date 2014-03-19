package com.kture.bpid;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;

/**
 * Created by Mariia_Lukianets on 18.03.14.
 */
public class RSAChiper {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    public RSAChiper() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); //1024 used for normal securities
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public String decodeText(String encodedText) {
        return encodedText;
    }

    private void decryptData(byte[] data) throws IOException {
        byte[] descryptedData = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            descryptedData = cipher.doFinal(data);
            System.out.println("Decrypted Data: " + new String(descryptedData));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}

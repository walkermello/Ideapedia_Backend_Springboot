package com.tugasakhir.ideapedia.security;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;


public class Crypto {

//    private static final String defaultKey = "12fc5e5b5abe431b94870823a154e511d2e3419f47c455a9659d89268776a3eb";
//    private static final String defaultKey = "7bf29d8a22a03f43baf953f5c5e4cb264dbf2db3651da21d28daee478a189cce";

    //28836c299e3515fb1e2b59cdd91fec54
//    private static final String defaultKey = "2d38e01c52c0d9c475dac174eff7cea6464778ec35f3d78eba5c1421d684a3e6";

    //942cb1c2439758f34fed2b971fc1c3d6
    private static final String defaultKey = "4c7304fbb592200766424b0334d359257a45cbb8737dee86d458be14ccac4cb4";

    /** 7e52334e09314807e67ed3882359adf0 --> e07169ee08959f7ba1118af6e607fcb3
     * U2FsdGVkX1/CwKWoBplvmTWuOfWkYAp+ICTG71sKZO0=
     * */
//    private static final String defaultKey = "7e52334e09314807e67ed3882359adf0";
    public static String performEncrypt(String keyText, String plainText) {
        try{
            byte[] key = Hex.decode(keyText.getBytes());
            byte[] ptBytes = plainText.getBytes();
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESLightEngine()));
            cipher.init(true, new KeyParameter(key));
            byte[] rv = new byte[cipher.getOutputSize(ptBytes.length)];
            int oLen = cipher.processBytes(ptBytes, 0, ptBytes.length, rv, 0);
            cipher.doFinal(rv, oLen);
            return new String(Hex.encode(rv));
        } catch(Exception e) {
            return "Error";
        }
    }

    public static String performEncrypt(String cryptoText) {
        return performEncrypt(defaultKey, cryptoText);
    }

    public static String performDecrypt(String keyText, String cryptoText) {
        try {
            byte[] key = Hex.decode(keyText.getBytes());
            byte[] cipherText = Hex.decode(cryptoText.getBytes());
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESLightEngine()));
            cipher.init(false, new KeyParameter(key));
            byte[] rv = new byte[cipher.getOutputSize(cipherText.length)];
            int oLen = cipher.processBytes(cipherText, 0, cipherText.length, rv, 0);
            cipher.doFinal(rv, oLen);
            return new String(rv).trim();
        } catch(Exception e) {
            return "Error";
        }
    }

    public static String performDecrypt(String cryptoText) {
        return performDecrypt(defaultKey, cryptoText);
    }

    public static void main(String[] args) {

//        Long timeMilis = System.currentTimeMillis();
//        System.out.println(timeMilis);
//        String strToEncrypt = "jdbc:mysql://mysqldb:3306/demo?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";//put text to encrypt in here
//        String strToEncrypt = "jdbc:mysql://mysqldb:3306/demo?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";//put text to encrypt in here
//        String strToEncrypt = "124|-|@$IA";//put text to encrypt in here
//        System.out.println("Encryption Result : "+performEncrypt(strToEncrypt));

//        String strToDecrypt = "U2FsdGVkX1/CwKWoBplvmTWuOfWkYAp+ICTG71sKZO0=";//put text to decrypt in here
        String strToDecrypt = "067417365277f3392940ba5b42bb538efb4c244f9520e1d41685d2ca0d3f60632d6ea87e5fc8c895b152664b7b38e6580c3241904501a3fc3a31bded21e35af4529bd3afb62c04cd8c7e112bc6e3612f6aa2f39426c0cd8f7195994af2ccdb7513a11e0d061b011732e9a63085dd78e3ff78738313b2bcecfb0242e866ba24c5c61a45238dbdb4dee59e17cfd3742294d275037841fa6bf4640ebb35b875c00d133b201f89b94cda0e790d3153d31648756792c9a6d309d50e066893c443804b710a173af339dfd9b30f565feb05a76428793ffcc4f8c4b72a8a9a4ddd88bae9b0b83a42dc08bd71a979153778f64884c2e3dc67c4509b21a90f3d8106adf16ddeb8c4b56de9ef075de8aff425dec2f93481b51d7ec1a83e4765cb828907ff2480d3ba39fc2d12f927c88b660379262ac1a57ef28635239d375dfc66b31483b79cae805c64a6dfcedd7b4d3dceba6c395179a74531c9bef65a33392ebb99ff036b751272548a333cf62aa1dfb1eeb07ffd1122e39436ce45c8d6cd1a79f03054";//put text to decrypt in here
        String decriptionResult = new Crypto().performDecrypt(strToDecrypt);
        System.out.println("Decryption Result : "+performDecrypt(strToDecrypt));
    }
}

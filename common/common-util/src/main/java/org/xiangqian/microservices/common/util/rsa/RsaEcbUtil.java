package org.xiangqian.microservices.common.util.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA是一种非对称加密算法，最初由Ron Rivest、Adi Shamir和Leonard Adleman在1977年提出。
 *
 * @author xiangqian
 * @date 20:58 2023/11/14
 */
public class RsaEcbUtil {

    /**
     * RSA
     */
    public static final I Default = new Abs("RSA");

    /**
     * RSA/ECB/NoPadding
     * 不使用填充方式，加密和解密的输入必须是固定长度的整数倍。填充时会在分组内容的前面填充0，直到内容的位数和模数相同。例如2048位的RSA，需要填充至256个字节。
     */
    public static final I NoPadding = new Abs("RSA/ECB/NoPadding");

    /**
     * RSA/ECB/PKCS1Padding
     * 最常见的模式，使用RSA算法进行加密和解密，并使用PKCS1填充方式进行填充。
     */
    public static final I PKCS1Padding = new Abs("RSA/ECB/PKCS1Padding");

    /**
     * RSA/ECB/OAEPWithSHA-1AndMGF1Padding
     * 算法使用OAEP填充方式，在SHA-1哈希函数和MGF1掩码生成函数的基础上进行加密和解密。
     * <p>
     * 使用OAEP（Optimal Asymmetric Encryption Padding）填充方案。
     * OAEP填充方案相对更安全，并且在实践中被广泛使用。
     */
    public static final I OAEPWithSHA1AndMGF1Padding = new Abs("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");

    /**
     * RSA/ECB/OAEPWithSHA-256AndMGF1Padding
     * 算法也使用OAEP填充方式，但使用SHA-256哈希函数和MGF1掩码生成函数进行加密和解密
     */
    public static final I OAEPWithSHA256AndMGF1Padding = new Abs("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

    private static class Abs implements I {
        private final String transformation;

        private Abs(String transformation) {
            this.transformation = transformation;
        }

        // RSA加密算法对数据块的长度是有限制的：
        // 1、对于公钥加密，数据块的最大长度不能超过密钥长度减去11字节。
        // 2、对于私钥解密，数据块的最大长度等于密钥长度。
        // 这意味着在使用1024位（128个字节）密钥时，公钥加密的数据块最大长度为117个字节，私钥解密的数据块最大长度为128个字节；
        // 而在使用2048位（256个字节）密钥时，公钥加密的数据块最大长度为245个字节，私钥解密的数据块最大长度为256个字节。

        @Override
        public byte[] encrypt(Key key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            boolean block = false;
            int length = data.length;
            boolean isPublicKey = false;
            boolean isPrivateKey = false;
            int keySize = 0;
            if (key instanceof RSAPublicKey) {
                isPublicKey = true;
                keySize = ((RSAPublicKey) key).getModulus().bitLength();
            } else if (key instanceof RSAPrivateKey) {
                isPrivateKey = true;
                keySize = ((RSAPrivateKey) key).getModulus().bitLength();
            }
            if (isPublicKey) {
                block = length > keySize - 11 * 8;
            } else if (isPrivateKey) {
                block = length > keySize;
            }
            if (block) {
                Cipher cipher = Cipher.getInstance(transformation);
                return null;
            }

            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        }

        private void blockEncrypt(Key key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//            Cipher cipher = Cipher.getInstance(transformation);
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//
//            int inputLen = data.length;
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            int offSet = 0;
//            byte[] cache;
//            int i = 0;
//
//            // Encrypt the data in segments
//            while (inputLen - offSet > 0) {
//                if (inputLen - offSet > 245) {
//                    cache = cipher.doFinal(data, offSet, 245);
//                } else {
//                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
//                }
//                out.write(cache, 0, cache.length);
//                i++;
//                offSet = i * 245;
//            }
//            byte[] encryptedData = out.toByteArray();
//            out.close();
//
//            return encryptedData;
        }

        @Override
        public byte[] decrypt(Key key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        }

        private void blockDecrypt() {
        }
    }

    public static interface I {
        /**
         * 加密
         *
         * @param key  密钥
         * @param data 待加密的数据
         * @return
         */
        byte[] encrypt(Key key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

        /**
         * 解密
         *
         * @param key  密钥
         * @param data 待解密的数据
         * @return
         */
        byte[] decrypt(Key key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;
    }

}

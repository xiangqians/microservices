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

        @Override
        public byte[] encrypt(Key key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            byte[] result = block(Cipher.ENCRYPT_MODE, key, data);
            if (result != null) {
                return result;
            }

            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        }

        @Override
        public byte[] decrypt(Key key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            byte[] result = block(Cipher.DECRYPT_MODE, key, data);
            if (result != null) {
                return result;
            }

            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        }

        /**
         * RSA加密算法对数据块的长度是有限制的：
         * 1、对于公钥加密，数据块的最大长度不能超过密钥长度减去11字节。
         * 2、对于私钥解密，数据块的最大长度等于密钥长度。
         * 这意味着在使用1024位（128个字节）密钥时，公钥加密的数据块最大长度为117个字节，私钥解密的数据块最大长度为128个字节；
         * 而在使用2048位（256个字节）密钥时，公钥加密的数据块最大长度为245个字节，私钥解密的数据块最大长度为256个字节。
         *
         * @param opmode
         * @param key
         * @param data
         * @return
         * @throws NoSuchPaddingException
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeyException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         */
        private byte[] block(int opmode, Key key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            // 密钥长度，单位：byte
            int keyLength = 0;

            // 数据长度，单位：byte
            int dataLength = data.length;

            // 最大块长度，单位：byte
            int maxBlockLength = 0;

            // 公钥
            if (key instanceof RSAPublicKey) {
                keyLength = ((RSAPublicKey) key).getModulus().bitLength() / 8;
                maxBlockLength = keyLength - 11;
            }
            // 私钥
            else if (key instanceof RSAPrivateKey) {
                keyLength = ((RSAPrivateKey) key).getModulus().bitLength() / 8;
                maxBlockLength = keyLength;
            }

            // 分块加密/解密
            if (dataLength > maxBlockLength) {
                Cipher cipher = Cipher.getInstance(transformation);
                cipher.init(opmode, key);

                // 已加密/解密的数据
                int arrIndex = 0;
                int arrLength = dataLength / maxBlockLength;
                if (dataLength % maxBlockLength != 0) {
                    arrLength += 1;
                }
                byte[][] arr = new byte[arrLength][];
                int resultLength = 0;

                // 加密
                int offset = 0;
                while (offset < dataLength) {
                    byte[] bytes = null;
                    int nextOffset = offset + maxBlockLength;
                    if (nextOffset <= dataLength) {
                        bytes = cipher.doFinal(data, offset, maxBlockLength);
                    } else {
                        bytes = cipher.doFinal(data, offset, dataLength - offset);
                    }
                    arr[arrIndex++] = bytes;
                    resultLength += bytes.length;
                    offset = nextOffset;
                }

                // 合并结果
                int resultIndex = 0;
                byte[] result = new byte[resultLength];
                for (int i = 0; i < arrLength; i++) {
                    byte[] bytes = arr[i];
                    int length = bytes.length;
                    System.arraycopy(bytes, 0, result, resultIndex, length);
                    resultIndex += length;
                }
                return result;
            }

            return null;
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

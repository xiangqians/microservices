package org.xiangqian.microservices.common.util.aes;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES/ECB 模式加解密
 * <p>
 * 注：
 * AES/ECB 模式下存在安全隐患，因此推荐使用更安全的分组密码模式，如 CBC、CTR 或 GCM。
 * <p>
 * AES（Advanced Encryption Standard）是一种常用的对称加密算法，常用的加密模式有以下几种：
 * 1、ECB（Electronic Codebook）模式：将明文分成固定大小的块，每个块独立地进行加密。但是，ECB模式存在一个问题，如果明文中有相同的块，则加密后的结果也会相同，这导致了一些安全性问题。
 * 2、CBC（Cipher Block Chaining）模式：在CBC模式中，前一个密文块会与当前明文块进行异或操作，然后再进行加密。这种方式可以解决ECB模式的问题，提高了安全性。
 * 3、CFB（Cipher Feedback）模式：CFB模式将前一个密文块作为输入来加密当前明文块，并将输出的密文块与明文块进行异或操作得到密文。这种模式实现了反馈机制，使得加密更加灵活。
 * 4、OFB（Output Feedback）模式：OFB模式类似于CFB模式，但它使用密钥流而不是密文块来进行加密。密钥流生成器产生的密钥流与明文进行异或操作得到密文。
 * 5、CTR（Counter）模式：CTR模式将一个计数器作为输入与密钥流生成器结合，然后将生成的密钥流与明文进行异或操作得到密文。CTR模式具有高度的并行性，因此在多核处理器上能够获得更好的性能。
 *
 * @author xiangqian
 * @date 19:00 2024/01/24
 */
public class AesEcbUtil {

    /**
     * AES/ECB/NoPadding
     * 该模式不进行任何填充，要求明文长度必须是块长度的整数倍。
     */
    public static final I NoPadding = new Abs("NoPadding");

    /**
     * AES/ECB/PKCS5Padding
     * PKCS5Padding 和 PKCS7Padding：这两种模式采用标准的 PKCS#5 和 PKCS#7 填充方式，根据块的大小进行适当的填充。
     */
    public static final I PKCS5Padding = new Abs("PKCS5Padding");

    /**
     * AES/ECB/PKCS7Padding
     */
    public static final I PKCS7Padding = new Abs("PKCS7Padding");

    /**
     * AES/ECB/ISO10126Padding
     * 该模式使用随机的填充字节，并在最后一个字节中包含填充的字节数。
     */
    public static final I ISO10126Padding = new Abs("ISO10126Padding");

    /**
     * AES/ECB/X9.23Padding
     * 该模式将填充字节设置为 0x00，并在最后一个字节中包含填充的字节数。
     */
    public static final I X923Padding = new Abs("X9.23Padding");

    private static class Abs implements I {
        private final String padding;

        private Abs(String padding) {
            this.padding = padding;
        }

        @Override
        public byte[] encrypt(byte[] data, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
            return encrypt(getCipher(padding), data, key);
        }

        @Override
        public byte[] decrypt(byte[] data, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
            return decrypt(getCipher(padding), data, key);
        }

        private byte[] encrypt(Cipher cipher, byte[] data, SecretKey key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        }

        private byte[] decrypt(Cipher cipher, byte[] data, SecretKey key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        }

        private Cipher getCipher(String padding) throws NoSuchPaddingException, NoSuchAlgorithmException {
            return Cipher.getInstance(String.format("AES/ECB/%s", padding));
        }
    }

    /**
     * 密码学
     */
    public static interface I {
        /**
         * 加密
         *
         * @param data 待加密数据
         * @param key  密钥
         * @return 密文
         * @throws NoSuchPaddingException
         * @throws NoSuchAlgorithmException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         * @throws InvalidKeyException
         */
        byte[] encrypt(byte[] data, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

        /**
         * 解密
         *
         * @param data 待解密数据
         * @param key  密钥
         * @return 原文
         * @throws NoSuchPaddingException
         * @throws NoSuchAlgorithmException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         * @throws InvalidKeyException
         */
        byte[] decrypt(byte[] data, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException;
    }

}
package org.xiangqian.microservices.common.util.aes;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES/CBC 模式加解密
 *
 * @author xiangqian
 * @date 19:08 2024/01/25
 */
public class AesCbcUtil {

    /**
     * AES/CBC/PKCS5Padding
     */
    public static final I PKCS5Padding = new Abs("PKCS5Padding");

    /**
     * AES/CBC/ISO10126Padding
     */
    public static final I ISO10126Padding = new Abs("ISO10126Padding");

    private static class Abs implements I {
        private final String padding;

        private Abs(String padding) {
            this.padding = padding;
        }

        @Override
        public byte[] encrypt(byte[] data, SecretKey key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            return encrypt(getCipher(padding), data, key, iv);
        }

        @Override
        public byte[] decrypt(byte[] data, SecretKey key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            return decrypt(getCipher(padding), data, key, iv);
        }

        private byte[] encrypt(Cipher cipher, byte[] data, SecretKey key, byte[] iv) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            IvParameterSpec params = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, params);
            return cipher.doFinal(data);
        }

        private byte[] decrypt(Cipher cipher, byte[] data, SecretKey key, byte[] iv) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
            IvParameterSpec params = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, params);
            return cipher.doFinal(data);
        }

        private Cipher getCipher(String padding) throws NoSuchPaddingException, NoSuchAlgorithmException {
            return Cipher.getInstance(String.format("AES/CBC/%s", padding));
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
         * @param iv   向量
         * @return 密文
         * @throws NoSuchPaddingException
         * @throws NoSuchAlgorithmException
         * @throws InvalidAlgorithmParameterException
         * @throws InvalidKeyException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         */
        byte[] encrypt(byte[] data, SecretKey key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

        /**
         * 解密
         *
         * @param data 待解密数据
         * @param key  密钥
         * @param iv   向量
         * @return 原文
         * @throws NoSuchPaddingException
         * @throws NoSuchAlgorithmException
         * @throws InvalidAlgorithmParameterException
         * @throws InvalidKeyException
         * @throws IllegalBlockSizeException
         * @throws BadPaddingException
         */
        byte[] decrypt(byte[] data, SecretKey key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;
    }

}
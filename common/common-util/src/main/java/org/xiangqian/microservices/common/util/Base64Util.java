package org.xiangqian.microservices.common.util;

import lombok.SneakyThrows;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author xiangqian
 * @date 19:29 2024/01/24
 */
public class Base64Util {

    /**
     * 字节数组编解码器
     */
    public static final Ed<byte[]> Bytes = new AbsEd<byte[]>() {
        @Override
        public byte[] encode(byte[] data) {
            return encode0(data);
        }

        @Override
        public String encodeToString(byte[] data) {
            return encodeToString0(data);
        }

        @Override
        public byte[] decode(byte[] data) {
            return decode0(data);
        }

        @Override
        public byte[] decode(String data) {
            return decode0(data);
        }
    };

    /**
     * {@link SecretKey} 编解码器
     */
    public static final Ed<SecretKey> SecretKey = new AbsEd<SecretKey>() {
        @Override
        public byte[] encode(SecretKey data) {
            return encode0(data.getEncoded());
        }

        @Override
        public String encodeToString(SecretKey data) {
            return encodeToString0(data.getEncoded());
        }

        @Override
        public SecretKey decode(byte[] data) {
            return newSecretKeySpec(decode0(data));
        }

        @Override
        public SecretKey decode(String data) {
            return newSecretKeySpec(decode0(data));
        }

        private SecretKeySpec newSecretKeySpec(byte[] encodedKey) {
            return new SecretKeySpec(encodedKey, "AES");
        }
    };

    /**
     * {@link PublicKey} 编解码器
     */
    public static final Ed<PublicKey> PublicKey = new AbsEd<PublicKey>() {
        @Override
        public byte[] encode(PublicKey data) {
            return encode0(data.getEncoded());
        }

        @Override
        public String encodeToString(PublicKey data) {
            return encodeToString0(data.getEncoded());
        }

        @Override
        public PublicKey decode(byte[] data) {
            return newPublicKey(decode0(data));
        }

        @Override
        public PublicKey decode(String data) {
            return newPublicKey(decode0(data));
        }

        @SneakyThrows
        private PublicKey newPublicKey(byte[] encodedKey) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
            return keyFactory.generatePublic(keySpec);
        }
    };

    /**
     * {@link PrivateKey} 编解码器
     */
    public static final Ed<PrivateKey> PrivateKey = new AbsEd<PrivateKey>() {
        @Override
        public byte[] encode(PrivateKey data) {
            return encode0(data.getEncoded());
        }

        @Override
        public String encodeToString(PrivateKey data) {
            return encodeToString0(data.getEncoded());
        }

        @Override
        public PrivateKey decode(byte[] data) {
            return newPrivateKey(decode0(data));
        }

        @Override
        public PrivateKey decode(String data) {
            return newPrivateKey(decode0(data));
        }

        @SneakyThrows
        private PrivateKey newPrivateKey(byte[] encodedKey) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
            return keyFactory.generatePrivate(keySpec);
        }
    };

    public static abstract class AbsEd<T> implements Ed<T> {
        protected final byte[] encode0(byte[] data) {
            return Base64.getEncoder().encode(data);
        }

        protected final String encodeToString0(byte[] data) {
            return Base64.getEncoder().encodeToString(data);
        }

        protected final byte[] decode0(byte[] data) {
            return Base64.getDecoder().decode(data);
        }

        protected final byte[] decode0(String data) {
            return Base64.getDecoder().decode(data);
        }
    }

    /**
     * 编解码器
     */
    public static interface Ed<T> {
        /**
         * 编码
         *
         * @param data 数据
         * @return
         */
        byte[] encode(T data);

        /**
         * 编码
         *
         * @param data 数据
         * @return
         */
        String encodeToString(T data);

        /**
         * 解码
         *
         * @param data 数据
         * @return
         */
        T decode(byte[] data);

        /**
         * 解码
         *
         * @param data 数据
         * @return
         */
        T decode(String data);
    }

}

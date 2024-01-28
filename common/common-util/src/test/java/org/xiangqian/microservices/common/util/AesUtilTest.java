package org.xiangqian.microservices.common.util;

import org.junit.Test;
import org.xiangqian.microservices.common.util.aes.AesCbcUtil;
import org.xiangqian.microservices.common.util.aes.AesEcbUtil;
import org.xiangqian.microservices.common.util.aes.AesKeyUtil;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * @author xiangqian
 * @date 19:58 2024/01/24
 */
public class AesUtilTest {

    @Test
    public void key() throws Exception {
        SecretKey key = AesKeyUtil.generate128BitKey();
        System.out.println(key);

        String encodedKey = Base64Util.SecretKey.encodeToString(key);
        System.out.println(encodedKey);

        key = Base64Util.SecretKey.decode(encodedKey);
        System.out.println(key);
    }

    @Test
    public void ecb() throws Exception {
        // 密钥
        String encodedKey = "+0u9BQACkxS2hEL3/XQlfw==";
        SecretKey key = Base64Util.SecretKey.decode(encodedKey);

        // 待加密数据
        byte[] data = "Hello, World!!!!".getBytes(StandardCharsets.UTF_8);

        ecb(AesEcbUtil.NoPadding, key, data);
        ecb(AesEcbUtil.PKCS5Padding, key, data);
//        ecb(AesEcbUtil.PKCS7Padding, key, data);
        ecb(AesEcbUtil.ISO10126Padding, key, data);
//        ecb(AesEcbUtil.X923Padding, key, data);
    }

    private void ecb(AesEcbUtil.I i, SecretKey key, byte[] data) throws Exception {
        // 加密
        byte[] encryptedData = i.encrypt(data, key);
        System.out.println(Base64Util.Bytes.encodeToString(encryptedData));

        // 解密
        data = i.decrypt(encryptedData, key);
        System.out.println(new String(data));
        System.out.println();
    }

    @Test
    public void cbc() throws Exception {
        // 密钥（128bit）
        String encodedKey = "r9S4vkJrmpy3HQwfvwmLhQ==";
        SecretKey key = Base64Util.SecretKey.decode(encodedKey);

        // 16字节的初始化向量
        final byte[] iv = "fedcba9876543210".getBytes(StandardCharsets.UTF_8);

        // 待加密数据
        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        cbc(AesCbcUtil.PKCS5Padding, key, iv, data);
        cbc(AesCbcUtil.ISO10126Padding, key, iv, data);
    }

    private void cbc(AesCbcUtil.I i, SecretKey key, byte[] iv, byte[] data) throws Exception {
        // 加密
        byte[] encryptedData = i.encrypt(data, key, iv);
        System.out.println(Base64Util.Bytes.encodeToString(encryptedData));

        // 解密
        data = i.decrypt(encryptedData, key, iv);
        System.out.println(new String(data));
        System.out.println();
    }

}

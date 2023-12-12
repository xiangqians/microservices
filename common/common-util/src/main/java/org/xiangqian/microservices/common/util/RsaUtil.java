package org.xiangqian.microservices.common.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.nio.Buffer;
import java.security.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

/**
 * RSA是一种非对称加密算法，最初由Ron Rivest、Adi Shamir和Leonard Adleman在1977年提出。
 *
 * @author xiangqian
 * @date 20:58 2023/11/14
 */
public class RsaUtil {

    // 密钥长度
    public static final int KEY_SIZE_512 = 512;   // 使用512位密钥
    public static final int KEY_SIZE_1024 = 1024; // 使用1024位密钥
    public static final int KEY_SIZE_2048 = 2048; // 使用2048位密钥
    public static final int KEY_SIZE_4096 = 4096; // 使用4096位密钥

    // 填充模式
    //
    // 填充模式分类：
    // 1、RSA-PKCS#1 v1.5：使用PKCS#1 v1.5填充方案
    // 2、RSA-OAEP：使用OAEP（Optimal Asymmetric Encryption Padding）填充方案
    // OAEP填充方案相对更安全，并且在实践中被广泛使用。
    //
    // RSA算法也是一个块加密算法（block cipher algorithm），总是在一个固定长度的块上进行操作，分组的长度是跟公私钥模长有关的。
    // RSA算法的填充是在每个分组前面填充的，而对称加密算法是在最后一个分组的后面进行填充。
    //
    public static final String DEFAULT_PADDING = "RSA";
    public static final String ECB_NO_PADDING = "RSA/ECB/NoPadding"; // 不使用填充方式，加密和解密的输入必须是固定长度的整数倍。填充时会在分组内容的前面填充0，直到内容的位数和模数相同。例如2048位的RSA，需要填充至256字节。
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding"; // 最常见的模式，使用RSA算法进行加密和解密，并使用PKCS1填充方式进行填充
    public static final String ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"; // 基于RSA算法的加密填充模式，使用SHA-1作为哈希函数，MGF1作为掩码生成函数
    public static final String ECB_OAEP_WITH_SHA256_AND_MGF1_PADDING = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"; // 基于RSA算法的加密填充模式，使用SHA-256作为哈希函数，MGF1作为掩码生成函数

    /**
     * 生成密钥对
     *
     * @param keySize 密钥长度
     * @return
     * @throws Exception
     */
    @SneakyThrows
    public static KeyPair generateKeyPair(int keySize) {
        // 获取默认的RSA实例
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        // 获取BC（Bouncy Castle）提供的RSA实例
//        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        // 获取SunRsaSign（Sun RSA Sign）提供的RSA实例
//        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "SunRsaSign");

        generator.initialize(keySize);

        return generator.generateKeyPair();
    }

    /**
     * @param cipher
     * @param data           数据
     * @param maxSegmentSize 最大分段大小
     * @return
     * @throws Exception
     */
    private static byte[] doFinal(Cipher cipher, byte[] data, int maxSegmentSize) throws Exception {
        int length = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream(length);

        // 对数据分段加密
        for (int offset = 0; offset < length; offset += maxSegmentSize) {
            byte[] bytes = cipher.doFinal(data, offset, Optional.of(offset + maxSegmentSize).filter(i -> i.intValue() < length).orElse(length - offset));
            out.write(bytes);
        }

        return out.toByteArray();
    }

    /**
     * 加密（使用公钥加密数据）
     *
     * @param paddingMode 填充模式 {@link RsaUtil#ECB_NO_PADDING}, {@link RsaUtil#ECB_PKCS1_PADDING}, {@link RsaUtil#ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING}, {@link RsaUtil#ECB_OAEP_WITH_SHA256_AND_MGF1_PADDING}
     * @param keySize     密钥长度
     * @param data        源数据
     * @param publicKey   公钥
     * @return base64数据
     * @throws Exception
     */
    public static String encrypt(String paddingMode, int keySize, byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(paddingMode);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int maxSegmentSize = keySize / 8 - 11;
        byte[] dataReturn = new byte[0];
        for (int i = 0; i < data.length; i += maxSegmentSize) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + maxSegmentSize));
            dataReturn = ArrayUtils.addAll(dataReturn, doFinal);
        }
        int maxSegmentSize = keySize / 8 - 11;


        byte[] bytes = cipher.doFinal(data);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解密（使用私钥解密数据）
     *
     * @param padding    填充算法 {@link RsaUtil#ECB_NO_PADDING}, {@link RsaUtil#ECB_PKCS1_PADDING}, {@link RsaUtil#ECB_OAEP_WITH_SHA1_AND_MGF1_PADDING}, {@link RsaUtil#ECB_OAEP_WITH_SHA256_AND_MGF1_PADDING}
     * @param maxSegment 密钥长度
     * @param data       base64数据
     * @param privateKey 私钥
     * @return 源数据
     * @throws Exception
     */
    public static byte[] decrypt(String padding, String data, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(data);
        Cipher cipher = Cipher.getInstance(padding);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int MAX_DECRYPT_BLOCK = KEY_SIZE_1024 / 8;
        // 解密时超过128字节就报错。为此采用分段解密的办法来解密
        StringBuffer sbuff = new StringBuffer("");
        for (int i = 0; i < bytes.length; i += MAX_DECRYPT_BLOCK) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(bytes, i, i + MAX_DECRYPT_BLOCK));
            sbuff.append(new String(doFinal).trim());
        }
        System.err.println(sbuff);

        return cipher.doFinal(bytes);
    }


    // RSA签名：使用私钥对数据进行签名，公钥验证签名的合法性。

}

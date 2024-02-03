package org.xiangqian.microservices.common.util.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Rsa密钥对工具
 *
 * @author xiangqian
 * @date 20:58 2023/11/14
 */
public class RsaKeyPairUtil {

    /**
     * 生成512位密钥对
     *
     * @return
     */
    public static KeyPair generate512BitKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair(512);
    }

    /**
     * 生成1024位密钥对
     *
     * @return
     */
    public static KeyPair generate1024BitKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair(1024);
    }

    /**
     * 生成2048位密钥对
     *
     * @return
     */
    public static KeyPair generate2048BitKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair(2048);
    }

    /**
     * 生成4096位密钥对
     *
     * @return
     */
    public static KeyPair generate4096BitKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair(4096);
    }

    /**
     * 生成密钥对
     *
     * @param bitLength 密钥长度，单位：bit
     * @return
     * @throws Exception
     */
    private static KeyPair generateKeyPair(int bitLength) throws NoSuchAlgorithmException {
        // 密钥算法提供者
        // 获取BC（Bouncy Castle）提供的RSA实例
        // String provider = "BC";
        // 获取SunRsaSign（Sun RSA Sign）提供的RSA实例
        //  String provider = "SunRsaSign";
        // java.security.KeyPairGenerator.getInstance(java.lang.String algorithm,  // 密钥算法
        //       java.lang.String provider) // 密钥提供者
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(bitLength);
        return generator.generateKeyPair();
    }

}

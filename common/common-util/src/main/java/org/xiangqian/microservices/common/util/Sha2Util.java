package org.xiangqian.microservices.common.util;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * SHA-2
 * 这是一个系列，包括SHA-224、SHA-256、SHA-384、SHA-512等多个变种，分别产生不同长度的哈希值，分别为224位、256位、384位和512位。SHA-256是其中最常用的版本之一。
 *
 * @author xiangqian
 * @date 20:36 2023/11/14
 */
public class Sha2Util {

    public static byte[] encrypt256(byte[] data) {
        return DigestUtils.sha256(data);
    }

    public static byte[] encrypt256(InputStream data) throws IOException {
        return DigestUtils.sha256(data);
    }

    public static byte[] encrypt256(String data) {
        return DigestUtils.sha256(data);
    }

    public static String encrypt256Hex(byte[] data) {
        return DigestUtils.sha256Hex(data);
    }

    public static String encrypt256Hex(InputStream data) throws IOException {
        return DigestUtils.sha256Hex(data);
    }

    public static String encrypt256Hex(String data) {
        return DigestUtils.sha256Hex(data);
    }

    @SneakyThrows
    public static String encrypt256Hex0(String data) {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(data.getBytes());
        return Hex.encodeHexString(hash);
    }

}

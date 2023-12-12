package org.xiangqian.microservices.common.util;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author xiangqian
 * @date 20:36 2023/11/14
 */
public class Md5Util {

    public static byte[] encrypt(byte[] data) {
        return DigestUtils.md5(data);
    }

    public static byte[] encrypt(InputStream data) throws IOException {
        return DigestUtils.md5(data);
    }

    public static byte[] encrypt(String data) {
        return DigestUtils.md5(data);
    }

    public static String encryptHex(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public static String encryptHex(InputStream data) throws IOException {
        return DigestUtils.md5Hex(data);
    }

    public static String encryptHex(String data) {
        return DigestUtils.md5Hex(data);
    }

    @Deprecated
    @SneakyThrows
    public static String encryptHex0(String data) {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hash = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

}

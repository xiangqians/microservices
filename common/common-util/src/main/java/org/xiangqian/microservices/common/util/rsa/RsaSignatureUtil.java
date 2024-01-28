package org.xiangqian.microservices.common.util.rsa;

import java.security.*;

/**
 * Rsa签名工具
 *
 * @author xiangqian
 * @date 19:29 2024/01/26
 */
public class RsaSignatureUtil {

    public static final I MD5withRSA = new Abs("MD5withRSA");
    public static final I SHA1withRSA = new Abs("SHA1withRSA");
    public static final I SHA256withRSA = new Abs("SHA256withRSA");

    private static class Abs implements I {
        private final String algorithm;

        private Abs(String algorithm) {
            this.algorithm = algorithm;
        }

        @Override
        public byte[] sign(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
            Signature s = getSignature();
            s.initSign(privateKey);
            s.update(data);
            return s.sign();
        }

        @Override
        public boolean verify(PublicKey publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
            Signature s = getSignature();
            s.initVerify(publicKey);
            s.update(data);
            return s.verify(signature);
        }

        /**
         * 获取签名实例
         *
         * @return
         */
        private Signature getSignature() throws NoSuchAlgorithmException {
            // java.security.Signature.getInstance(java.lang.String algorithm, // 签名算法
            //        java.lang.String provider) // 签名算法提供者
            return Signature.getInstance(algorithm);
        }
    }

    public static interface I {
        /**
         * 生成签名（使用私钥生成签名）
         *
         * @param privateKey 私钥
         * @param data       待签名的数据
         * @return 签名值
         * @throws InvalidKeyException
         * @throws SignatureException
         */
        byte[] sign(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;

        /**
         * 验证签名（使用公钥验证签名）
         *
         * @param publicKey 公钥
         * @param data      待验证的数据
         * @param signature 签名值
         * @return
         * @throws InvalidKeyException
         * @throws SignatureException
         */
        boolean verify(PublicKey publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
    }

}

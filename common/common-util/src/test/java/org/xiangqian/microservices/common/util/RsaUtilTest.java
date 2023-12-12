package org.xiangqian.microservices.common.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author xiangqian
 * @date 20:11 2023/11/14
 */
public class RsaUtilTest {

    @Test
    public void keyPair() {
        KeyPair keyPair = RsaUtil.generateKeyPair(RsaUtil.KEY_SIZE_2048);
        System.out.format("Public Key : %s", Base64.getEncoder().encodeToString((keyPair.getPublic().getEncoded()))).println();
        System.out.format("Private Key: %s", Base64.getEncoder().encodeToString((keyPair.getPrivate().getEncoded()))).println();
    }

    @SneakyThrows
    @Test
    public void ed() {
        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
//        data = """
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                Pm/IbV8+7y+efNNQDV8Kz9f0IeATo5lyOXNgiOsa8ck50Ft1lkU9gdef0TKmfONQ/xYPsEIEN4A+Jh9EjFwLJ/shaWQnl7mZFM2xF0wLFstVs2dDW6wKp4QhA078/+DWq257axL2t4BgdJrCuf3n1jEXU9r2ZAaJZNaoqXPzhxY=
//                """.getBytes(StandardCharsets.UTF_8);
        KeyPair keyPair = RsaUtil.generateKeyPair(RsaUtil.KEY_SIZE_1024);

        System.out.format("Public Key : %s", Base64.getEncoder().encodeToString((keyPair.getPublic().getEncoded()))).println();
        System.out.format("Private Key: %s", Base64.getEncoder().encodeToString((keyPair.getPrivate().getEncoded()))).println();

        // 使用公钥进行加密
        String encryptedData = RsaUtil.encrypt(RsaUtil.ECB_NO_PADDING, data, keyPair.getPublic());
//        System.out.format("Encrypted Data: %s", encoder(Base64.getEncoder().encodeToString((keyPair.getPublic().getEncoded())), "Hello, World! ")).println();
        System.out.format("Encrypted Data: %s", encryptedData).println();

        // 使用私钥进行解密
        byte[] decryptedData = RsaUtil.decrypt(RsaUtil.ECB_NO_PADDING, encryptedData, keyPair.getPrivate());
        System.out.format("Decrypted Data: %s", decoder(Base64.getEncoder().encodeToString((keyPair.getPrivate().getEncoded())), encryptedData)).println();
        System.out.format("Decrypted Data: %s", new String(decryptedData, StandardCharsets.UTF_8)).println();
    }


    /**
     * RSA 公钥加密
     *
     * @param publicKey RSA公钥Key
     * @param src       需加密数据源
     * @return
     */
    public static String encoder(String publicKey, String src) {
        try {
            // Cipher类为加密和解密提供密码功能，通过getinstance实例化对象
            Cipher cipher = Cipher.getInstance(RSANOPADDING);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            int MAX_ENCRYPT_BLOCK = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8 - 11;
            byte[] data = src.getBytes(CHARSET);
            byte[] dataReturn = new byte[0];
            for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
                dataReturn = ArrayUtils.addAll(dataReturn, doFinal);
            }
            return Base64.getEncoder().encodeToString(dataReturn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA 私钥加密方法
     *
     * @param privateKey RSA私钥Key
     * @param src        需要加密的数据
     * @return
     */
    public static String decoder(String privateKey, String src) {
        try {
            byte[] data = Base64.getDecoder().decode(src);
            // Cipher类为加密和解密提供密码功能，通过getinstance实例化对象
            Cipher cipher = Cipher.getInstance(RSANOPADDING);
            // 初始化解密
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            int MAX_DECRYPT_BLOCK = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8;
            // 解密时超过128字节就报错。为此采用分段解密的办法来解密
            StringBuffer sbuff = new StringBuffer("");
            for (int i = 0; i < data.length; i += MAX_DECRYPT_BLOCK) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_DECRYPT_BLOCK));
                sbuff.append(new String(doFinal, CHARSET).trim());
            }
            return sbuff.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //公钥字符串 转公钥
    public static PublicKey getPublicKey(String pubKey) throws Exception {
        byte[] buffer = Base64.getDecoder().decode(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec
                keySpec = new X509EncodedKeySpec(buffer);
        return keyFactory.generatePublic(keySpec);
    }

    //私钥字符串 转私钥
    public static PrivateKey getPrivateKey(String priKey) throws Exception {
        byte[] buffer = Base64.getDecoder().decode(priKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    private static final String CHARSET = "UTF-8";
    private static final String KEY_ALGORITHM = "RSA";
    private static final String RSANOPADDING = "RSA/ECB/NoPadding";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    //RSA 签名算法
    private static final String ALGORITHM_RSA_SIGN = "SHA256WithRSA";
    private static final int ALGORITHM_RSA_PRIVATE_KEY_LENGTH = 1024;

}

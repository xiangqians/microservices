package org.xiangqian.microservices.common.util;

import org.junit.Test;
import org.xiangqian.microservices.common.util.rsa.RsaEcbUtil;
import org.xiangqian.microservices.common.util.rsa.RsaKeyPairUtil;
import org.xiangqian.microservices.common.util.rsa.RsaSignatureUtil;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author xiangqian
 * @date 20:11 2023/11/14
 */
public class RsaUtilTest {

    @Test
    public void keyPair() throws Exception {
        KeyPair keyPair = RsaKeyPairUtil.generate512BitKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        System.out.println(publicKey);
        String encodedPublicKey = Base64Util.PublicKey.encodeToString(publicKey);
        System.out.println(encodedPublicKey);
        publicKey = Base64Util.PublicKey.decode(encodedPublicKey);
        System.out.println(publicKey);
        System.out.println();

        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println(privateKey);
        String encodedPrivateKey = Base64Util.PrivateKey.encodeToString(privateKey);
        System.out.println(encodedPrivateKey);
        privateKey = Base64Util.PrivateKey.decode(encodedPrivateKey);
        System.out.println(privateKey);
    }

    @Test
    public void signature() throws Exception {
        // 密钥对
        KeyPair keyPair = RsaKeyPairUtil.generate1024BitKeyPair();

        // 待签名的数据
        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        signature(RsaSignatureUtil.MD5withRSA, keyPair, data);
        signature(RsaSignatureUtil.SHA1withRSA, keyPair, data);
        signature(RsaSignatureUtil.SHA256withRSA, keyPair, data);
    }

    private void signature(RsaSignatureUtil.I i, KeyPair keyPair, byte[] data) throws Exception {
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 生成签名
        byte[] signature = i.sign(privateKey, data);
        System.out.println(Base64Util.Bytes.encodeToString(signature));

        // 验证签名
        boolean verified = i.verify(publicKey, data, signature);
        System.out.println(verified);
        System.out.println();
    }

    @Test
    public void ecb() throws Exception {
        // 密钥对
        KeyPair keyPair = RsaKeyPairUtil.generate1024BitKeyPair();

        // 待加密的数据
        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        boolean longer = true;
        if (longer) {
            data = """
                    春夜宴桃李园序 / 春夜宴从弟桃花园序
                    【作者】李白 【朝代】唐
                    夫天地者，万物之逆旅也；光阴者，百代之过客也。
                    而浮生若梦，为欢几何？
                    古人秉烛夜游，良有以也。
                    况阳春召我以烟景，大块假我以文章。
                    会桃花之芳园，序天伦之乐事。
                    群季俊秀，皆为惠连；吾人咏歌，独惭康乐。
                    幽赏未已，高谈转清。
                    开琼筵以坐花，飞羽觞而醉月。
                    不有佳咏，何伸雅怀？
                    如诗不成，罚依金谷酒数。
                    """.getBytes(StandardCharsets.UTF_8);
        }

        ecb(RsaEcbUtil.Default, keyPair, data);
        ecb(RsaEcbUtil.NoPadding, keyPair, data);
        ecb(RsaEcbUtil.PKCS1Padding, keyPair, data);
//        ecb(RsaEcbUtil.OAEPWithSHA1AndMGF1Padding, keyPair, data);
//        ecb(RsaEcbUtil.OAEPWithSHA256AndMGF1Padding, keyPair, data);
    }

    private void ecb(RsaEcbUtil.I i, KeyPair keyPair, byte[] data) throws Exception {
        ecb1(i, keyPair, data);
    }

    private void ecb1(RsaEcbUtil.I i, KeyPair keyPair, byte[] data) throws Exception {
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 使用公钥进行加密
        byte[] encryptedData = null;
        encryptedData = i.encrypt(publicKey, data);
        System.out.format("Encrypted Data: %s", Base64.getEncoder().encodeToString(encryptedData)).println();

        // 使用私钥进行解密
        byte[] decryptedData = i.decrypt(privateKey, encryptedData);
        System.out.format("Decrypted Data: %s", new String(decryptedData)).println();
        System.out.println();
    }

    private void ecb2(RsaEcbUtil.I i, KeyPair keyPair, byte[] data) throws Exception {
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 使用私钥进行加密
        byte[] encryptedData = null;
        encryptedData = i.encrypt(privateKey, data);
        System.out.format("Encrypted Data: %s", Base64.getEncoder().encodeToString(encryptedData)).println();

        // 使用公钥进行解密
        byte[] decryptedData = i.decrypt(publicKey, encryptedData);
        System.out.format("Decrypted Data: %s", new String(decryptedData)).println();
        System.out.println();
    }

}
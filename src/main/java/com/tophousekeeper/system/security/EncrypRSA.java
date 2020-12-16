package com.tophousekeeper.system.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author NiceBin
 * @description: RSA加密算法，服务器启动时会生成一对秘钥
 *               所以秘钥有效期为下次服务器重启为止
 * @date 2019/9/2 21:07
 */
public class EncrypRSA {

    private static Logger logger = LoggerFactory.getLogger(EncrypRSA.class);

    //秘钥
    public static Key convertSecretKey;

    /**
     * 秘钥初始化
     * @return
     */
    public static void init() throws Exception{
        logger.info("开始生成秘钥");
        //生成秘钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(56);
        // 产生秘钥
        SecretKey secretKey = keyGenerator.generateKey();
        // 获取秘钥
        byte[] bytesKey = secretKey.getEncoded();

        // KEY转换
        DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        Key convertSecretKey = factory.generateSecret(desKeySpec);

        EncrypRSA.convertSecretKey = convertSecretKey;
        logger.info("秘钥生成完毕");
    }

    /**
     * 加密
     *
     * @param src 要加密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encrypt(String src) throws Exception {

        if (src == null) return null;

        if (convertSecretKey != null) {

            // 加密（加解密方式：..工作模式/填充方式）
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
            //必须得这种编码，不然转为String再转回来byte内容是不同的！！
            byte[] result = cipher.doFinal(src.getBytes(StandardCharsets.ISO_8859_1));

            return new String(result, StandardCharsets.ISO_8859_1);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param src 要解密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decrypt(String src) throws Exception {

        if (src == null) return null;

        if (convertSecretKey != null) {

            // 加密（加解密方式：..工作模式/填充方式）
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
            byte[] result = cipher.doFinal(src.getBytes(StandardCharsets.ISO_8859_1));
            return new String(result,StandardCharsets.ISO_8859_1);
        }
        return null;
    }
}

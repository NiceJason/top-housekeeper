package com.tophousekeeper.system.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author NiceBin
 * @description: RSA加密算法，服务器启动时会生成一对秘钥，目前只在用在登录时加密cookie
 * 所以秘钥有效期为下次服务器重启为止
 * @date 2019/9/2 21:07
 */
public class EncrypRSA {

    public static Key convertSecretKey;

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

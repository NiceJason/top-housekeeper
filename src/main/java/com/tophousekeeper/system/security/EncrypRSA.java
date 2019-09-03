package com.tophousekeeper.system.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author NiceBin
 * @description: RSA加密算法，服务器启动时会生成一对秘钥，目前只在用在登录时加密cookie
 * 所以秘钥有效期为下次服务器重启为止
 * @date 2019/9/2 21:07
 */
public class EncrypRSA {

    public static RSAPrivateKey privateKey;
    public static RSAPublicKey publicKey;

    /**
     * 加密
     * @param publicKey 公钥
     * @param src   要加密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encrypt(RSAPublicKey publicKey, String src) throws Exception{

        byte[] srcBytes = src.getBytes("UTF-8");

        if (publicKey != null) {

            //Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");

            //根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] resultBytes = cipher.doFinal(srcBytes);

            return new String(resultBytes,"UTF-8");
        }
        return null;
    }

    /**
     * 解密
     * @param privateKey    私钥
     * @param src   要解密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decrypt(RSAPrivateKey privateKey, String src) throws Exception{

        byte[] srcBytes = src.getBytes("UTF-8");

        if (privateKey != null) {

            //Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");

            //根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] resultBytes = cipher.doFinal(srcBytes);
            return new String(resultBytes,"UTF-8");
        }
        return null;
    }
}

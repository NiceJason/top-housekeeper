package com.tophousekeeper.util;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.security.EncrypRSA;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Collections;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther: NiceBin
 * @description: Jwt构造器，创建Token来进行身份记录
 * jwt由3个部分构成：jwt头，有效载荷（主体,payLoad），签名
 * @date: 2020/5/7 22:40
 */
public class JwtTool {

    //以下为JwtTool生成时的主题
    //登录是否还有效
    public static final String SUBJECT_ONLINE_STATE = "online_state";

    //以下为载荷固定的Key值
    //主题
    public static final String SUBJECT = "subject";
    //发布时间
    public static final String TIME_ISSUED = "timeIssued";
    //过期时间
    public static final String EXPIRATION = "expiration";

    /**
     * 生成token，参数都是载荷（自定义内容）
     * 其中Map里为非必要数据，而其他参数为必要参数
     *
     * @param subject  主题，token生成干啥用的，用上面的常量作为参数
     * @param liveTime 存活时间(秒单位)，建议使用TimeUnit方便转换
     *                 如TimeUnit.HOURS.toSeconds(1);将1小时转为秒 = 3600
     * @param claimMap 自定义荷载，可以为空
     * @return
     */
    public static String createToken(String subject, long liveTime, HashMap<String, String> claimMap) throws Exception {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //毫秒要转为秒
        long now = System.currentTimeMillis() / 1000;

//        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(EncrypRSA.keyString);
//
//        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
                //加密算法
                .setHeaderParam("alg", "HS256")
                //jwt签名
                .signWith(signatureAlgorithm, EncrypRSA.convertSecretKey);

        HashMap<String,String> payLoadMap = new HashMap<>();
        payLoadMap.put(SUBJECT,subject);
        payLoadMap.put(TIME_ISSUED,String.valueOf(now));
        //设置Token的过期时间
        if (liveTime >= 0) {
            long expiration = now + liveTime;
            payLoadMap.put(EXPIRATION,String.valueOf(expiration));
        } else {
            throw new SystemException(SystemStaticValue.TOOL_PARAMETER_EXCEPTION_CODE, "liveTime参数异常");
        }

        StringBuilder payLoad = new StringBuilder();



        if (!Collections.isEmpty(claimMap)) {
            payLoadMap.putAll(claimMap);
        }

        //拼接主题payLoad，采用 key1,value1,key2,value2的格式
        for (Map.Entry<String, String> entry : payLoadMap.entrySet()) {
            payLoad.append(entry.getKey()).append(',').append(entry.getValue()).append(',');
        }

        //对payLoad进行加密，这样别人Base64URL解密后也不是明文
        String encrypPayLoad = EncrypRSA.encrypt(payLoad.toString());

        jwtBuilder.setPayload(encrypPayLoad);

        //会自己生成签名，组装
        return jwtBuilder.compact();
    }

    /**
     * 私钥解密token信息
     *
     * @param token
     * @return 存有之前定义的Key, value的Map，解析失败则返回null
     */
    public static HashMap getMap(String token) {
        if (!Tool.isNull(token)) {
            try {
                String encrypPayLoad = Jwts.parser()
                        .setSigningKey(EncrypRSA.convertSecretKey)
                        .parsePlaintextJws(token).getBody();

                String payLoad = EncrypRSA.decrypt(encrypPayLoad);

                String[] payLoads = payLoad.split(",");
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < payLoads.length - 1; i=i+2) {
                    map.put(payLoads[i], payLoads[i + 1]);
                }
                return map;
            } catch (Exception e) {
                System.out.println("Token解析失败");
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 判断token是否有效
     *
     * @param map 已经解析过token的map
     * @return true 为有效
     */
    public static boolean isAlive(HashMap<String, String> map) {

        if (!Collections.isEmpty(map)) {
            String tokenString = map.get(EXPIRATION);

            if (!Tool.isNull(tokenString)) {
                long expiration = Long.valueOf(tokenString) / 1000;
                long now = System.currentTimeMillis();
                if (expiration > now) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断token是否有效
     * @param token 还未被解析的token
     * @return
     */
    public static boolean isAlive(String token) {
        return JwtTool.isAlive(JwtTool.getMap(token));
    }
}

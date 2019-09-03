package com.tophousekeeper.system;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * @author NiceBin
 * @description: 工具类
 *               1.快速搭建Json字符串
 *               2.给定范围出随机数
 *               3.判断字符串是否为整数
 *               4.判断字符串是否为数字或字母
 *               5.判断字符串是否为空
 *               6.判断字符串是否是邮箱
 *               7.判断字符串是否是Web地址
 * @date 2019/6/24 18:49
 */
public class Tool {

    /**
     * 快速构建JSONObjet，将想要的值转为Json串
     * @param keyAndValues 更多的key和value，必须成对出现
     * @return
     */
    public static String quickJson(String... keyAndValues){
           JSONObject jsonObject = new JSONObject();
           if(keyAndValues!=null){
               //不是大于等于2的偶数则格式错误
               if(keyAndValues.length%2!=0||keyAndValues.length<2){
                   throw new SystemException(SystemStaticValue.TOOL_PARAMETER_EXCEPTION_CODE,"quickJson参数错误");
               }
               for(int i =0 ;i < keyAndValues.length;i=i+2){
                   jsonObject.put(keyAndValues[i],keyAndValues[i+1]);
               }
           }
           return jsonObject.toJSONString();
    }

    /**
     * 获得一定范围的安全随机数
     * @param min 最小值，能转为数值型就行
     * @param max 最大值，能转为数值型就行
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static int getSecureRandom(Object min,Object max) throws NoSuchAlgorithmException {
        int theMin=0,theMax=0;
        //检测大小值是否合法
        boolean legalMin = false,legalMax = false;

        if(min instanceof String){
            theMin=Integer.parseInt((String)min);
            legalMin=true;
        }
        if(max instanceof String){
            theMax=Integer.parseInt((String)max);
            legalMax=true;
        }
        if(min instanceof Integer){
            theMin=(Integer)min;
            legalMin=true;
        }
        if(max instanceof Integer){
            theMax=(Integer)max;
            legalMax=true;
        }

        if(!(legalMax&&legalMin)){
           throw new SystemException(SystemStaticValue.TOOL_PARAMETER_EXCEPTION_CODE,"随机数参数不正确");
        }

        //这种方法获取的随机数最安全
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        //nextInt值生成0到n之间的数，包含0不包含n，所以要+1来包含
        return secureRandom.nextInt(theMax - theMin +1)+theMin;
    }

    /**
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否是数字或字母
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str){
        Pattern pattern = Pattern.compile("^[a-z0-9A-Z]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否是邮箱
     * @param str
     * @return
     */
    public static boolean isEmail(String str){
        Pattern pattern = Pattern.compile("\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isNull(String str){
        if(str == null || "".equals(str)){
            return true;
        }
        else return false;
    }

    /**
     * 判断字符串是否是Web地址
     * @param str
     * @return
     */
    public static boolean isWebURL(String str){
        Pattern pattern = Pattern.compile("(http|https):\\/\\/([\\w.]+\\/?)\\S*");
        return pattern.matcher(str).matches();
    }

    /**
     * 根据key来获取cookie的值
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request,String key) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // 遍历数组
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    // 取出cookie的值
                    String value = cookie.getValue();
                    //解码，因为cookie设置值时都进行编码
                    return URLDecoder.decode(value,"UTF-8");
                }
            }
        }
        return null;
    }
}

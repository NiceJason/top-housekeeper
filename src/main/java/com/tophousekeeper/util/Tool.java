package com.tophousekeeper.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * @author NiceBin
 * @description: 工具类
 * 1.快速搭建Json字符串
 * 2.给定范围出随机数
 * 3.判断字符串是否为整数
 * 4.判断字符串是否为数字或字母
 * 5.判断字符串是否为空
 * 6.判断字符串是否是邮箱
 * 7.判断字符串是否是Web地址
 * 8.根据key来获取cookie的值
 * 9.根据key来清除cookie的值
 * 10.根据key,value来设置cookie的值
 * 11.String转对象
 * 12.beanToString
 * 13.获取ip地址
 * 14.获得代理类方法中真实的方法
 * @date 2019/6/24 18:49
 */
public class Tool {

    /**
     * 快速构建JSONObjet，将想要的值转为Json串
     *
     * @param keyAndValues 更多的key和value，必须成对出现
     * @return
     */
    public static String quickJson(String... keyAndValues) {
        JSONObject jsonObject = new JSONObject();
        if (keyAndValues != null) {
            //不是大于等于2的偶数则格式错误
            if (keyAndValues.length % 2 != 0 || keyAndValues.length < 2) {
                throw new SystemException(SystemStaticValue.TOOL_PARAMETER_EXCEPTION_CODE, "quickJson参数错误");
            }
            for (int i = 0; i < keyAndValues.length; i = i + 2) {
                jsonObject.put(keyAndValues[i], keyAndValues[i + 1]);
            }
        }
        return jsonObject.toJSONString();
    }

    /**
     * 获得一定范围的安全随机数，随后转为int
     *
     * @param min 最小值，能转为数值型就行
     * @param max 最大值，能转为数值型就行
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static int getSecureRandom(Object min, Object max) throws NoSuchAlgorithmException {
        int theMin = 0, theMax = 0;
        //检测大小值是否合法
        boolean legalMin = false, legalMax = false;

        if (min instanceof String) {
            theMin = Integer.parseInt((String) min);
            legalMin = true;
        }else if (min instanceof Integer) {
            theMin = (Integer) min;
            legalMin = true;
        }else if(min instanceof Long){
            theMin = ((Long) min).intValue();
            legalMin = true;
        }

        if (max instanceof String) {
            theMax = Integer.parseInt((String) max);
            legalMax = true;
        }else if (max instanceof Integer) {
            theMax = (Integer) max;
            legalMax = true;
        }else if(max instanceof Long){
            theMax = ((Long) max).intValue();
            legalMax = true;
        }



        if (!(legalMax && legalMin)) {
            throw new SystemException(SystemStaticValue.TOOL_PARAMETER_EXCEPTION_CODE, "随机数参数不正确");
        }

        //这种方法获取的随机数最安全
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        //nextInt值生成0到n之间的数，包含0不包含n，所以要+1来包含
        return secureRandom.nextInt(theMax - theMin + 1) + theMin;
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否是数字或字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        Pattern pattern = Pattern.compile("^[a-z0-9A-Z]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        Pattern pattern = Pattern.compile("\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if (str == null || "".equals(str)) {
            return true;
        } else return false;
    }

    /**
     * 判断字符串是否是Web地址
     *
     * @param str
     * @return
     */
    public static boolean isWebURL(String str) {
        Pattern pattern = Pattern.compile("(http|https):\\/\\/([\\w.]+\\/?)\\S*");
        return pattern.matcher(str).matches();
    }

    /**
     * 根据key来获取cookie的值
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // 遍历数组
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    // 取出cookie的值
                    String value = cookie.getValue();
                    //解码，因为cookie设置值时都进行编码
                    return URLDecoder.decode(value, "UTF-8");
                }
            }
        }
        return null;
    }

    /**
     * 根据key来清除cookie的值
     *
     * @param request
     * @param key
     * @return
     */
    public static void clearCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // 遍历数组
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * 根据key,value来设置cookie的值
     * @param response
     * @param key
     * @param value
     * @param aliveTime cookie最大存活时间，秒为单位
     * @return
     */
    public static void setCookie(HttpServletResponse response, String key, String value,int aliveTime) {
        Cookie cookie = new Cookie(key, value);
        //需要重新设置属性，因为它不会按原来的属性存的，需要把一切设置好，再addCookie，不然设置无效
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(aliveTime);
        response.addCookie(cookie);
    }

    /**
     * String转对象
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String value, Class<T> clazz) {
        if(value==null||value.length()<=0||clazz==null){
            return null;
        }

        if(clazz ==int.class ||clazz==Integer.class){
            return (T)Integer.valueOf(value);
        }
        else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(value);
        }
        else if(clazz==String.class){
            return (T)value;
        }else {
            //这一步是转为对象的
            return JSON.toJavaObject(JSON.parseObject(value),clazz);
        }
    }

    /**
     * 对象转String
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToString(T value) {

        if(value==null){
            return null;
        }
        Class <?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }
        else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }
        else if(clazz==String.class){
            return (String)value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 获取与服务器相连的ip，可能是用户的ip，也可能是代理的ip
     * @param request
     * @return
     */
    public static String getConnectIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，最后一个ip是与服务器相连的ip
            if( ip.indexOf(",")!=-1 ){
                String[] ips = ip.split(",");
                ip = ips[ips.length-1];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获得代理类方法中真实的方法
     * 小知识：
     * ClassUtils.getMostSpecificMethod(Method method, Class<?> targetClass)
     * 该方法是一个有趣的方法，他能从代理对象上的一个方法，找到真实对象上对应的方法。
     * 举个例子，MyComponent代理之后的对象上的someLogic方法，肯定是属于cglib代理之后的类上的method，
     * 使用这个method是没法去执行目标MyComponent的someLogic方法，
     * 这种情况下，就可以使用getMostSpecificMethod，
     * 找到真实对象上的someLogic方法，并执行真实方法
     *
     * BridgeMethodResolver.findBridgedMethod(Method bridgeMethod)
     * 如果当前方法是一个泛型方法，则会找Class文件中实际实现的方法
     * @param poxyMethod 代理的方法
     * @param targetclass 真实的目标类
     * @return
     */
    public static Method getSpecificMethod(Method poxyMethod,Class targetclass){
        Method specificMethod = ClassUtils.getMostSpecificMethod(poxyMethod,targetclass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        return specificMethod;
    }

    /**
     * 获得代理类方法中真实的方法
     * 小知识：
     * AopProxyUtils.ultimateTargetClass()
     * 获取一个代理对象的最终对象类型
     * @param joinPoint 切面的切点类
     * @return
     */
    public static Method getSpecificMethod(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method poxyMethod = methodSignature.getMethod();
        Class targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());
        return Tool.getSpecificMethod(poxyMethod,targetClass);
    }
}

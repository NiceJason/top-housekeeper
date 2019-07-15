package com.tophousekeeper.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author NiceBin
 * @description: 工具类
 *               1.快速搭建Json字符串
 *               2.给定范围出随机数
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
                   throw new SystemException("100","quickJson参数错误");
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
           throw new SystemException("100","随机数参数不正确");
        }

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        return secureRandom.nextInt((theMax - theMin +1)+theMin);
    }
}

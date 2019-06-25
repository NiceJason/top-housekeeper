package com.tophousekeeper.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author NiceBin
 * @description: 工具类
 *               1.快速搭建Json字符串
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
}

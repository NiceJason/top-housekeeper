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
     * 快速构建JSONObjet，将想要的值转为Json串,替换"为'
     * @param key
     * @param value
     * @return
     */
    public static String quickJson(String key, String value){
           JSONObject jsonObject = new JSONObject();
           jsonObject.put(key,value);
           return jsonObject.toJSONString();
    }
}

package com.tophousekeeper.system.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author NiceBin
 * @description: 系统资源类,是资源类的父类
 * @date 2019/8/12 18:59
 */
public abstract class SystemResource implements I_Resource{
    //资源id
    protected String id;
    //资源名称
    protected String name;
    //资源内容
    protected String content;
    //资源类型
    protected String type;

    //------------------以下属性与数据库无关，用于程序运行
    //是否已经数据解析
    protected boolean isParse = false;
    //数据解析后存放的Map,在调用此Map的时候，如果未解析，会进行数据解析
    protected Map<String,String> parsingMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 从解析后的Map中获取指定细分资源
     * @param key 该细分资源的名称
     * @return
     */
    public String getValue(String key){
        String value = null;
        if(isParse){
            value = parsingMap.get(key);
        }else{
            initParsingMap();
            isParse = true;
            value = parsingMap.get(key);
        }
        return value;
    }

    /**
     * 解析content的内容，获得细分资源
     */
    public abstract void initParsingMap();
}

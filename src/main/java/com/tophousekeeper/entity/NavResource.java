package com.tophousekeeper.entity;

import com.tophousekeeper.system.resource.SystemResource;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Table;

/**
 * @author NiceBin
 * @description: 导航资源类
 * @date 2019/8/14 11:11
 */
@Table(name = "t_system_resource")
public class NavResource extends SystemResource implements Comparable<NavResource>{

    //------------以下为导航类型
    public static final String TYPE_NAV_CATALOG = "Nav-catalog";
    public static final String TYPE_NAV_ITEM = "Nav-item";
    //------------以下为细分资源的key值
    //目录位置
    public static final String KEY_CATALOG_INDEX = "KEY_CATALOG_INDEX";
    //项目的内容
    public static final String KEY_IDS = "KEY_IDS";
    //项目样式-分界
    public static final String KEY_SPLIT = "KEY_SPLIT";
    //项目链接
    public static final String KEY_HREF = "KEY_HREF";

    @Override
    public void initParsingMap() {
        if(content!=null){
            String[] subdivision = content.split(";");

            //目录的解析
            if(TYPE_NAV_CATALOG.equals(type)){
                parsingMap.put(KEY_CATALOG_INDEX,subdivision[0]);
                parsingMap.put(KEY_IDS,subdivision[1]);
            }else if(TYPE_NAV_ITEM.equals(type)){ //非目录解析
                parsingMap.put(KEY_SPLIT,subdivision[0]);
                parsingMap.put(KEY_HREF,subdivision[1]);
            }
        }
    }

    @Override
    public int compareTo(@NotNull NavResource o) {
        int index0 = Integer.valueOf(this.getValue(KEY_CATALOG_INDEX));
        int index1 = Integer.valueOf(o.getValue(KEY_CATALOG_INDEX));
        return index0 - index1;
    }
}

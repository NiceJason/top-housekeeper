package com.tophousekeeper.entity;

import javax.persistence.Table;

/**
 * @author NiceBin
 * @description: 系统资源类
 * @date 2019/8/12 18:59
 */
@Table(name = "t_system_resource")
public class SystemResource {
    //资源id
    private String id;
    //资源名称
    private String name;
    //资源内容
    private String content;
    //资源类型
    private String type;

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
}

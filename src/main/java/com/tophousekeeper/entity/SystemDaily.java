package com.tophousekeeper.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author NiceBin
 * @description: 系统每日的资源
 * @date 2019/10/31 16:40
 */
@Table(name = "t_system_daily")
public class SystemDaily {

    //系统每日登录人数
    @Column(name = "login_count")
    private Integer loginCount;
    //今日访问网站的人数
    @Column(name = "online_count")
    private Integer onlineCount;
    //今日日期
    private Timestamp date;

    public SystemDaily(){
        ////将String类型格式化为timestamp
        Date toDate = new Date();
        date = new Timestamp(toDate.getTime());
    }

    //以下为set和get


    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }
}

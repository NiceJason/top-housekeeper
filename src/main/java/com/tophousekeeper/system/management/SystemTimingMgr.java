package com.tophousekeeper.system.management;

import com.tophousekeeper.dao.function.system.SystemDailyDao;
import com.tophousekeeper.entity.SystemDaily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author NiceBin
 * @description: 系统资源的管理器
 * @date 2019/11/5 11:45
 */
@Component
public class SystemTimingMgr {
    @Autowired
    SystemDailyDao systemDailyDao;
    @Autowired
    SystemDaily systemDaily;
    //保存每日数据
    public void saveSystemDaily(){
        systemDailyDao.insert(systemDaily);
    }
}

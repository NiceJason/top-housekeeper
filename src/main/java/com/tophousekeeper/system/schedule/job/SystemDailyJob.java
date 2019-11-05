package com.tophousekeeper.system.schedule.job;

import com.tophousekeeper.system.management.SystemTimingMgr;
import com.tophousekeeper.system.running.SystemContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/10/30 17:16
 */
public class SystemDailyJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("SystemDailyJob执行了 "+new Date().toString());
        ApplicationContext applicationContext = SystemContext.getSystemContext().getApplicationContext();
        SystemTimingMgr systemTimingMgr = (SystemTimingMgr)applicationContext.getBean("systemTimingMgr");
        systemTimingMgr.saveSystemDaily();
    }
}

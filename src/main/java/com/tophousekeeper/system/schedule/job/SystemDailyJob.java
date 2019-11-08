package com.tophousekeeper.system.schedule.job;

import com.tophousekeeper.system.management.SystemTimingMgr;
import com.tophousekeeper.system.running.SystemContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @author NiceBin
 * @description: JOB按正常来说Spring是管不了的，就是自动注入，AOP切面都不行
 *               可以监听实现JobListener或者TriggerListener接口来看调度过程（包括抛异常）
 *               https://blog.csdn.net/weixin_34410662/article/details/91433565
 * @date 2019/10/30 17:16
 */
public class SystemDailyJob extends QuartzJobBean {

    SystemTimingMgr systemTimingMgr;

    public SystemDailyJob(){
        systemTimingMgr = SystemContext.getSystemContext().getAppBean(SystemTimingMgr.class);
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("SystemDailyJob执行了 "+new Date().toString());
        try {
            systemTimingMgr.saveSystemDaily();
        } catch (Exception e) {
            throw new JobExecutionException(e.getMessage());
        }
    }
}

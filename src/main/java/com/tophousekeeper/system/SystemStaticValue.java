package com.tophousekeeper.system;

import java.util.concurrent.TimeUnit;

/**
 * @author NiceBin
 * @description: 记录着共同的系统常量
 * @date 2019/6/24 8:43
 */
public class SystemStaticValue {
    //以下为一些系统的固定值
    //ajax请求返回操作Key值
    public static final String ACTION_RESULT = "action_result";
    //ajax需要跳转的界面
    public static final String REDIRECT_URL = "redirect_url";

    //以下为线程池的配置（THREAD开头）
    //核心（能一起执行）的线程数
    public static int THREAD_CORE_POOL_SIZE = 20;
    //最大线程数
    public static int THREAD_MAXIMUM_POOL_SIZE = 40;
    //非核心线程的存活时间
    public static long THREAD_KEEP_ALIVE_TIME = 2;
    //KEEP_ALIVE_TIME的单位
    public static TimeUnit THREAD_UNIT = TimeUnit.MINUTES;

    //以下为缓存信息的配置(CACHE开头)--------------------------------------------------------
    //系统缓存名称及过期时间（秒）
    public enum SystemCache{
        //每日缓存,有效时间24小时
        DAY("dailyCache",60),
        //半日缓存，有效时间12小时
        HALF_DAY("halfDayCache",12*60*60),
        //1小时缓存
        ONE_HOUR("oneHour",1*60*60),
        //半小时缓存
        HALF_HOUR("halfHour",30*60);
        private String cacheName;
        private long surviveTime;

        SystemCache(String cacheName,long surviveTime){
            this.cacheName = cacheName;
            this.surviveTime = surviveTime;
        }

        public String getCacheName() {
            return cacheName;
        }

        public void setCacheName(String cacheName) {
            this.cacheName = cacheName;
        }

        public long getSurviveTime() {
            return surviveTime;
        }

        public void setSurviveTime(long surviveTime) {
            this.surviveTime = surviveTime;
        }
    }

    //系统缓存过期时间允许最小值（秒）
    public static final long CACHE_MIN_EXPIRE = 2*60;
    //系统缓存过期时间允许最大值（秒）
    public static final long CACHE_MAX_EXPIRE= 7*60;
    //以下为缓存数据的key（固定的key需要用''包裹）
    public static final String CACHE_ID_WELCOMENAVEGATION = "'welcomeNavegation'";

    //以下为系统资源，SY开头，存于SystemContext的resouces(Map对象)中------------------------
    //系统每日资源
    public static final String SY_DAILY = "system_daily";
    
    //以下为验证码资源-------------------------------------------------------------------
    //图片验证码的图片池
    public static final String[] IDENTIFYING_IMG_POOL = {"/image/ver-1.png","/image/ver-2.png","/image/ver-3.png"};
    //验证码过期时间（毫秒）
    public static final int IDENTIFYING_OVERDUE = 20000;
    //非法验证次数的key
    public static final String IDENTIFYING_ILLEGAL_COUNT = "illegalCount";
    //非法验证次数的标准值
    public static final int IDENTIFYING_ILLEGAL_STANDARD = 3;
    //非法验证封禁的时间（分钟）
    public static final int IDENTIFYING_ILLEGAL_BAN = 1;
    //以下为验证码业务
    //登录业务
    public static final String IDENTIFYING_FUNC_LOGIN = "login";
    //注册业务
    public static final String IDENTIFYING_FUNC_REGISTERED = "register";

    //以下为异常状态码，600开始，EXCEPTION_CODE结尾----------------------------------------------------------
    //系统异常
    public static final String SYSTEM_EXCEPTION_CODE = "600";
    //工具类参数异常
    public static final String TOOL_PARAMETER_EXCEPTION_CODE = "601";
    //权限异常
    public static final String PERMISSIONS_EXCEPTION = "602";
    //数据库异常
    public static final String DATASOURCE_EXCEPTION = "603";
    //验证异常
    public static final String IDENTIFYING_EXCEPTION_CODE = "631";
    //登录异常
    public static final String LOGIN_EXCEPTION_CODE = "640";
    //注册异常
    public static final String REGISTERED_EXCEPTION_CODE = "641";
    //邮箱格式异常
    public static final String EMAIL_EXCEPTION_CODE = "642";
    //密码格式异常
    public static final String PASSWORD_EXCEPTION_CODE = "643";
    //Web地址异常
    public static final String WEBURL_EXCEPTION_CODE = "644";
    //非法访问异常
    public static final String Illegal_EXCEPTION_CODE = "645";
    //内存异常
    public static final String CACHE_EXCEPTION_CODE = "646";
}

package com.tophousekeeper.system;

/**
 * @author NiceBin
 * @description: 记录着共同的系统常量
 * @date 2019/6/24 8:43
 */
public class SystemStaticValue {
    //ajax请求返回操作Key值
    public static final String ACTION_RESULT = "action_result";
    //ajax需要跳转的界面
    public static final String REDIRECT_URL = "redirect_url";

    //以下为系统资源，SY开头，存于SystemContext的resouces(Map对象)中
    //在线登录的人数
    public static final String SY_ONLINE = "online";

    //以下为系统redis缓存的名称,RE(reids)开头---------------------------------------------
    public static final String RE_WELCOMENAVEGATION = "welcomeNavegation";

    //以下为验证码资源-------------------------------------------------------------------
    //图片验证码的图片池
    public static final String[] IDENTIFYING_IMG_POOL = {"/image/ver-1.png","/image/ver-2.png","/image/ver-3.png"};
    //验证码过期时间（毫秒）
    public static final int IDENTIFYING_OVERDUE = 20000;
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
}

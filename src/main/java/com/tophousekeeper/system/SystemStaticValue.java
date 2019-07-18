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

    //以下为验证码资源
    //图片验证码的图片池
    public static final String[] IDENTIFYING_IMG_POOL = {"/image/ver-1.png","/image/ver-2.png","/image/ver-3.png"};
    //验证码过期时间（毫秒）
    public static final int IDENTIFYING_OVERDUE = 10000;
    //以下为验证码业务
    //登录业务
    public static final String IDENTIFYING_FUNC_LOGIN = "login";
    //注册业务
    public static final String IDENTIFYING_FUNC_REGISTERED = "register";
}

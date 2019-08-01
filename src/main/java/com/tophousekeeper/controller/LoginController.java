package com.tophousekeeper.controller;

import com.tophousekeeper.entity.ImgIdentifying;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import com.tophousekeeper.system.security.I_Identifying;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/3 19:02
 */
@Controller
@RequestMapping("/access")
public class LoginController {

    //存储进Session变量Key
    //用户
    private static String USER = "user";

    @Autowired
    LoginService loginService;

    /**
     * 注册
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/registered")
    public String registered(HttpServletRequest request) throws Exception {
        loginService.registered(request);
        return Tool.quickJson(SystemStaticValue.ACTION_RESULT, "注册成功");
    }

    /**
     * 登录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        User user = loginService.login(request);
        //检测是否有此账号信息
        if(user!=null){
            HttpSession session = request.getSession(true);
            session.setAttribute(USER, user);
            return Tool.quickJson(SystemStaticValue.ACTION_RESULT, "登录成功",
                    SystemStaticValue.REDIRECT_URL, "/welcome");
        }
        throw new SystemException(SystemStaticValue.LOGIN_EXCEPTION_CODE,"账号或密码错误");

    }

    /**
     * 注销
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, ModelAndView modelAndView) {

        RedisTemplate redisTemplate = new RedisTemplate();
        request.getSession().removeAttribute(USER);
        modelAndView.addObject(SystemStaticValue.ACTION_RESULT, "注销成功");
        modelAndView.setViewName("/welcome/welcome");
        return modelAndView;
    }

    /**
     * 获取验证码信息
     * @param request
     * @return
     * @throws NoSuchAlgorithmException
     */
    @ResponseBody
    @RequestMapping("/identifying")
    public String getIdentifying(HttpServletRequest request) throws NoSuchAlgorithmException {
           ImgIdentifying identifying = new ImgIdentifying().getInstance(request);
           HttpSession session = request.getSession();
           //存入验证类
           session.setAttribute(I_Identifying.IDENTIFYING,identifying);
           return JSONObject.fromObject(identifying).toString();
    }
}

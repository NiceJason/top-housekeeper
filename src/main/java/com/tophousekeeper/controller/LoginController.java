package com.tophousekeeper.controller;

import com.alibaba.fastjson.JSONObject;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/3 19:02
 */
@Controller
@RequestMapping("/access")
public class LoginController {

    @Autowired
    LoginService loginService;

    @ResponseBody
    @RequestMapping("/registered")
    public String registered(@RequestBody User user) throws Exception {
        loginService.registered(user);
        return Tool.quickJson(SystemStaticValue.ACTION_RESULT,"注册成功");
    }

    @ResponseBody
    @RequestMapping("/login")
    public User login(User checkUser) {
        return loginService.login(checkUser);

    }
}

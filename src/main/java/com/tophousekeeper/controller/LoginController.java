package com.tophousekeeper.controller;

import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/registered")
    public String registered(User user) throws Exception {
           loginService.registered(user);
           return "/welcome/welcome";
    }

    @ResponseBody
    @RequestMapping("/login")
    public User login(User checkUser){
        return loginService.login(checkUser);
    }
}

package com.tophousekeeper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/3 19:02
 */
@Controller
@RequestMapping("/access")
public class LoginController {

    @RequestMapping("/register")
    public String register(@RequestParam String email,@RequestParam String password){
        System.out.println("进入注册入口");
        return null;
    }
}

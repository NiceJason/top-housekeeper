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
public class LoginController {

    @RequestMapping("/registered")
    public String registered(@RequestParam String email,@RequestParam String password){
           return null;
    }
}

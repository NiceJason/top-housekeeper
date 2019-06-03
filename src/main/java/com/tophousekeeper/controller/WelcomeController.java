package com.tophousekeeper.controller;

import com.tophousekeeper.service.welcome.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {

    @Autowired
    private WelcomeService welcomeService;

    @RequestMapping(value={"/","/index","welcome"})
    public String welcome(){
        System.out.println("进入欢迎页面");
        System.out.println(welcomeService.selectByResourcesId(1));
        return "/welcome/welcome";
    }

}

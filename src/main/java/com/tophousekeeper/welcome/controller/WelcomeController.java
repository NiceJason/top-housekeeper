package com.tophousekeeper.welcome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WelcomeController {

//    @RequestMapping("/")
//    public String welcome(){
//        System.out.println("进入欢迎页面人造卫星");
//        return "欢迎!!妈的！！";
//    }

    @RequestMapping("/")
    public String welcome(){
        System.out.println("进入欢迎页面");
//        return new ModelAndView("welcome");
        return "welcome";
    }

    @RequestMapping("/welcome2")
    public String welcome2(HttpServletRequest request, HttpServletResponse response){
        System.out.println("进入2");
      return "welcome2";
    }

}

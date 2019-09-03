package com.tophousekeeper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WelcomeController {

    @RequestMapping(value={"/welcome","/"})
    public ModelAndView welcome(HttpServletRequest request,ModelAndView modelAndView){

        modelAndView.setViewName("/welcome/welcome");
        return modelAndView;
    }

}

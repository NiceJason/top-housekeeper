package com.tophousekeeper.controller.system;

import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/8/15 10:30
 */
@Controller
@RequestMapping("/Navegation")
public class NavegationController {

    @RequestMapping("/jumpURL")
    public ModelAndView jumpURL(HttpServletRequest request,ModelAndView modelAndView){
           String src = request.getParameter("src");
           //因为是form表单来提交跳转的，所以不抛出异常了，不然不会跳转到异常界面
           if(!Tool.isWebURL(src)){
               modelAndView.setViewName("error");
               modelAndView.addObject("code",SystemStaticValue.WEBURL_EXCEPTION_CODE);
               modelAndView.addObject("msg","WebURL无效");
           }else{
               modelAndView.addObject("blogSrc",src);
               modelAndView.setViewName("/content/blog");
           }
           return modelAndView;
    }
}

package com.tophousekeeper.controller.system;

import com.tophousekeeper.system.SystemStaticValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auther: NiceBin
 * @description: Controller增强，包括
 *               ajax异常页面重定向
 * @date: 2019/6/13 21:00
 */
@ControllerAdvice
@RequestMapping("/system")
public class SystemControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(SystemControllerAdvice.class);

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * webDataBinder是用于表单到方法的数据绑定的！
     * @param binder
     */
      @InitBinder
      public void initBinder(WebDataBinder binder){
          //将String转为Date
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          CustomDateEditor dateEditor = new CustomDateEditor(df, true);
          binder.registerCustomEditor(Date.class,dateEditor);
      }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "djb");
    }

    @RequestMapping(value = {"/error/{code}/{msg}","/error"})
    public ModelAndView errorPage(@PathVariable(value="code",required = false) String code,
                                  @PathVariable(value="msg",required = false) String msg){

           ModelAndView modelAndView=new ModelAndView();
           modelAndView.setViewName("/error_page");
           modelAndView.addObject("code",code);
           //如果系统出错误了
           if(SystemStaticValue.SYSTEM_EXCEPTION_CODE.equals(code)){
               modelAndView.addObject("msg","Sorry，系统出错了");
           }
           modelAndView.addObject("msg",msg);
           return modelAndView;
    }
}

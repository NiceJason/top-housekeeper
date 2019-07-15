package com.tophousekeeper.controller.system;

import com.tophousekeeper.system.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther: NiceBin
 * @description: Controller增强，包括
 *               1.异常统一收集，自定义错误码100
 *               2.ajax异常页面重定向
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

      }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "djb");
    }

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String,Object>> errorHandler(Exception ex){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        logger.error(ex.getMessage());
        return new ResponseEntity<>(map,HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
    @ExceptionHandler(value = SystemException.class)
    public ResponseEntity<Map<String,Object>> myErrorHandler(SystemException ex){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        logger.error(ex.getMsg());
        return new ResponseEntity<>(map,HttpStatus.EXPECTATION_FAILED);
    }

    @RequestMapping("/error/{code}")
    public ModelAndView errorPage(SystemException ex){
           logger.error("报错跳转 "+ex.getMsg());
           ModelAndView modelAndView=new ModelAndView();
           modelAndView.setViewName("error");
           modelAndView.addObject("code",ex.getCode());
           modelAndView.addObject("msg","处理过的错误提示");
           return modelAndView;
    }
}

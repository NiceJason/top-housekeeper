package com.tophousekeeper.controller.system;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
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
 *               1.异常统一收集，若不是自定义异常，则状态码为600（系统异常）
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
        map.put("code", SystemStaticValue.SYSTEM_EXCEPTION_CODE);
        map.put("msg", ex.getMessage());
        logger.error(ex.getLocalizedMessage()+" 错误:"+ex.getMessage());
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
        map.put("code", ex.getCode());
        map.put("msg", ex.getMsg());
        logger.error(ex.getMsg());
        return new ResponseEntity<>(map,HttpStatus.EXPECTATION_FAILED);
    }

    @RequestMapping("/error/{code}/{msg}")
    public ModelAndView errorPage(@PathVariable(value="code",required = false) String code,
                                  @PathVariable(value="msg",required = false) String msg){

           ModelAndView modelAndView=new ModelAndView();
           modelAndView.setViewName("error");
           modelAndView.addObject("code",code);
           //如果系统出错误了
           if(SystemStaticValue.SYSTEM_EXCEPTION_CODE.equals(code)){
               modelAndView.addObject("msg","Sorry，系统出错了");
           }
           modelAndView.addObject("msg",msg);
           return modelAndView;
    }
}

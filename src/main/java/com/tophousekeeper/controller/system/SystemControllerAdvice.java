package com.tophousekeeper.controller.system;

import com.tophousekeeper.system.SystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther: NiceBin
 * @description: Controller增强，包括异常统一收集
 * @date: 2019/6/13 21:00
 */
@ControllerAdvice
public class SystemControllerAdvice {
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
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());

        return map;
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = SystemException.class)
    public Map myErrorHandler(SystemException ex) {
        Map map = new HashMap();
        map.put("code", ex.getCode());
        map.put("msg", ex.getMsg());
        return map;
    }

}

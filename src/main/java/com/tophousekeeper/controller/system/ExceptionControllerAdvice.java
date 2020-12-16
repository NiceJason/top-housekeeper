package com.tophousekeeper.controller.system;

import com.tophousekeeper.entity.system.Response;
import com.tophousekeeper.system.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;

/**
 * @auther: NiceBin
 * @description:  异常统一拦截进行反馈
 * 在运行时从上往下依次调用每个异常处理方法
 * 匹配当前异常类型是否与@ExceptionHandler注解所定义的异常相匹配
 * 若匹配，则执行该方法，同时忽略后续所有的异常处理方法
 * 最终会返回经JSON序列化后的Response对象
 * @date: 2020/4/22 08:59
 */
@ControllerAdvice
@ResponseBody
public class ExceptionControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败", e);
        return new Response().failure("could_not_read_json");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法", e);
        return new Response().failure("request_method_not_supported");
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Response handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error("不支持当前媒体类型", e);
        return new Response().failure("content_type_not_supported");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Response handleValidationException(Exception e){
        logger.error("参数验证失败", e);
        return new Response().failure("validation_exception");
    }

    /**
     * 500 - 自定义程序错误
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SystemException.class)
    public Response handleSystemException(SystemException e){
//        errorChangePage(request,modelAndView);
        logger.error("系统自定义报错 "+e.getCode()+","+e.getMsg(),e);
        return new Response().failure(e.getMsg());
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
//        errorChangePage(request,modelAndView);
        logger.error("服务运行异常", e);
        return new Response().failure("服务运行异常");
    }

//    private final String ajaxHeader = "XMLHttpRequest";
//    /**
//     * 由于非ajax直接访问的时候，产生错误不会跳转到错误页面，所有由此来指定
//     */
//    private void errorChangePage( HttpServletRequest request, ModelAndView modelAndView,String code,String msg){
//        System.out.println("------------非ajax请求报错，页面自动跳转至错误页面");
//        String xReq = request.getHeader("x-requested-with");
//        if (Tool.isNull(xReq) || !ajaxHeader.equalsIgnoreCase(xReq)) {
//            modelAndView.setViewName(SystemStaticValue.ERROR_PAGE_PATH+"/"+code+"/"+msg);
//        }
//    }
}

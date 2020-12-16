//package com.tophousekeeper.system.Filter;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import java.io.IOException;
//
///**
// * @author NiceBin
// * @description: 测试Filter顺序的类
// * @date 2019/6/27 16:13
// */
//@WebFilter(filterName = "aF", urlPatterns = "/*", asyncSupported = true)
//public class aF implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("执行a1");
//        chain.doFilter(request,response);
//        System.out.println("执行a2");
//    }
//
//}
//

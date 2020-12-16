package com.tophousekeeper.util;

import com.tophousekeeper.system.SystemStaticValue;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @auther: NiceBin
 * @description: 跟前后端交互相关的方法类在此
 *               1.获取请求中的Body内容
 *               2.重定向到错误页面
 * @date: 2020/4/23 19:56
 */
public class HttpHelper {
    /**
     * 获取请求中的Body内容
     * @param request
     * @return
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static boolean errorPageDirect(HttpServletResponse response, String code, String msg) throws IOException {
        String encodeMsg = URLEncoder.encode(msg,"UTF-8");
        response.sendRedirect(SystemStaticValue.ERROR_PAGE_PATH+"/"+code+"/"+encodeMsg);
        return false;
    }
}


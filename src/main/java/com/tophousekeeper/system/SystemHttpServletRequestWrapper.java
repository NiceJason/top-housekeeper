package com.tophousekeeper.system;

import com.tophousekeeper.util.HttpHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;

/**
 * @auther: NiceBin
 * @description: 包装的httpServlet，进行以下增强
 *               1.将流数据取出保存，方便多次读出
 *               2.防止XSS攻击，修改读取数据的方法，过滤敏感字符
 * @date: 2020/4/23 19:50
 */
public class SystemHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;
    private HttpServletRequest request;

    public SystemHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        //打印属性
        //printRequestAll(request);
        //过滤一下，json字符串转换会调用此方法
        String bodyStr = StringEscapeUtils.escapeHtml4(HttpHelper.getBodyString(request));
        body = bodyStr.getBytes(Charset.forName("UTF-8"));

        this.request = request;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    /**
     * 可以打印出HttpServletRequest里属性的值
     * @param request
     */
    public void printRequestAll(HttpServletRequest request){
        Enumeration e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " = " + value);
        }
    }

    //以下为XSS预防
    /**
     * 直接用request去获取参数会用此方法
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        String value = request.getParameter(name);
        if (!StringUtils.isEmpty(value)) {
            value = StringEscapeUtils.escapeHtml4(value);
        }
        return value;
    }

    /**
     * 用SpringMVC注解获取参数会走此方法
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String value = parameterValues[i];
            parameterValues[i] = StringEscapeUtils.escapeHtml4(value);
        }
        return parameterValues;
    }
}

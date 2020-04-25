package com.tophousekeeper.controller.otherSystemTest;

import com.tophousekeeper.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author NiceBin
 * @description: 浙江分行的测试
 * @date 2019/12/16 15:09
 */
@Controller
@RequestMapping("/otherTest/zjfh")
public class zjfhTest {

    @RequestMapping("/parseXML")
    public void parseXML(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");


        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        MultipartFile mulFile = req.getFiles("file").get(0);

        byte[] content = mulFile.getBytes();

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String time = simpleDateFormat.format(date);

        File file = new File(time+"_"+Tool.getSecureRandom(1,10)+"_ResultXML.xml");
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
            outputStream.write(content);
            outputStream.flush();
        } finally {
            outputStream.close();
        }

        response.setHeader("content-type", "text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><DOCUMENT><MSG><CODE>S000</CODE><DESC>文件接收成功</DESC></MSG></DOCUMENT>");

    }
}

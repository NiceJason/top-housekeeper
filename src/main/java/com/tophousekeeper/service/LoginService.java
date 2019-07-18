package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.Identifying;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/18 8:15
 */
@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    public void registered(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        loginDao.insert(user);
    }

    public User login(HttpServletRequest request) {
        String password = request.getParameter("password");
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orCondition("password = ", password);
        User user = loginDao.selectOneByExample(example);
        return user;
    }

    public Identifying getIdentifying(HttpServletRequest request) throws NoSuchAlgorithmException {
        String min_X = request.getParameter("min_X");
        String max_X = request.getParameter("max_X");
        String min_Y = request.getParameter("min_Y");
        String max_Y = request.getParameter("max_Y");

        int imgPoolLength = SystemStaticValue.IDENTIFYING_IMG_POOL.length;
        String imgSrc = SystemStaticValue.IDENTIFYING_IMG_POOL[Tool.getSecureRandom(0, imgPoolLength - 1)];
        int X = Tool.getSecureRandom(min_X, max_X);
        int Y = Tool.getSecureRandom(min_Y, max_Y);

        Identifying identifying = new Identifying(String.valueOf(Tool.getSecureRandom("0", "100")));
        identifying.setImgSrc(imgSrc);
        identifying.setX(X);
        identifying.setY(Y);
        Calendar calendar = Calendar.getInstance();
        identifying.setCalendar(calendar);
        identifying.setIdentifyingType(request.getParameter("identifyingType"));
        return identifying;
    }

    public void checkIdentifying(HttpServletRequest request) {
        String identifyingId = request.getParameter("identifyingId");
        String moveEnd_X = request.getParameter("moveEnd_X");
        String identifyingType = request.getParameter("identifyingType");

        if(identifyingId==null||identifyingType==null||!Tool.isInteger(moveEnd_X)){
            throw new SystemException("100","验证失败");
        }

        HttpSession session = request.getSession();
        Identifying identifying = (Identifying) session.getAttribute("identifying");

        //开始验证，先验证时间是否在有效期
        Calendar nowCalendar = Calendar.getInstance();
        if(nowCalendar.getTimeInMillis()-identifying.getCalendar().getTimeInMillis()
                >SystemStaticValue.IDENTIFYING_OVERDUE){
            throw new SystemException("100","验证码过期");
        }
        //验证类型
        if(!identifying.getIdentifyingType().equals(identifyingType)){
            throw new SystemException("100","验证码失败");
        }
        //验证Id
        if(!identifying.getIdentifyingId().equals(identifyingId)){
            throw new SystemException("100","验证码失败");
        }
        //跟前端判断保持一致
        int X = identifying.getX()-10;
        int move = Integer.parseInt(moveEnd_X);
        if((X+identifying.getX()) < move || move < (X-identifying.getX())){
            throw new SystemException("100","验证失败");
        }
    }

}

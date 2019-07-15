package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.Identifying;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/18 8:15
 */
@Service
public class LoginService {

       @Autowired
       private LoginDao loginDao;

       public void registered(User user){
           loginDao.insert(user);
       }

       public User login(User checkUser) {
           Example example = new Example(User.class);
           Example.Criteria criteria = example.createCriteria();
           criteria.orCondition("password = ", checkUser.getPassword());
           User user = loginDao.selectOneByExample(example);
           return user;
       }

       public String getIdentifying(HttpServletRequest request) throws NoSuchAlgorithmException {
           String min_X = request.getParameter("min_X");
           String max_X = request.getParameter("max_X");
           String min_Y = request.getParameter("min_Y");
           String max_Y = request.getParameter("max_Y");

           Identifying identifying = new Identifying();

           int imgPoolLength = SystemStaticValue.IDENTIFYING_IMG_POOL.length;
           identifying.setImgSrc(SystemStaticValue.IDENTIFYING_IMG_POOL[Tool.getSecureRandom(0,imgPoolLength-1)]);
           identifying.setX(Tool.getSecureRandom(min_X,max_X));
           identifying.setY(Tool.getSecureRandom(min_Y,max_Y));
           return JSONObject.fromObject(identifying).toString();
       }

}

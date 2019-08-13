package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.system.WelcomeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/8/12 19:40
 */
@Service
public class WelcomeService {
    @Autowired
    private WelcomeDao welcomeDao;

    public String selectById(int resourcesId){
        return welcomeDao.selectByPrimaryKey(resourcesId).getInfo();
    }
}

package com.tophousekeeper.service;

import com.tophousekeeper.dao.WelcomeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/5/24 8:01
 */
@Service
public class WelcomeService {

    @Autowired
    private WelcomeDao welcomeDao;


    public String selectByResourcesId(int resourcesId){
        return welcomeDao.selectByPrimaryKey(resourcesId).getInfo();
    }
}

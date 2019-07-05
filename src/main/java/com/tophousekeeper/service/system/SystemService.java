package com.tophousekeeper.service.system;

import com.tophousekeeper.dao.function.system.SystemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/5/24 8:01
 */
@Service
public class SystemService {

    @Autowired
    private SystemDao systemDao;


    public String selectByResourcesId(int resourcesId){
        return systemDao.selectByPrimaryKey(resourcesId).getInfo();
    }
}

package com.tophousekeeper.service.welcome;

import com.tophousekeeper.TopHousekeeperApplication;
import com.tophousekeeper.dao.funtionDao.welcome.WelcomeDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TopHousekeeperApplication.class})
public class WelComeServiceTest {

    @Autowired
    private WelcomeDao welcomeDao;

    @Test
    public void selectByResourcesId() {
        System.out.println(welcomeDao.selectByPrimaryKey(1).toString());
    }



}
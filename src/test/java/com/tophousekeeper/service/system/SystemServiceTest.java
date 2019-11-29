package com.tophousekeeper.service.system;

import com.tophousekeeper.TopHousekeeperApplicationTests;
import com.tophousekeeper.system.SystemException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemServiceTest extends TopHousekeeperApplicationTests {

    @Autowired
    SystemService systemService;

    @Test
    public void test() {
           throw  new SystemException(null,null);
    }

    public void test2(){
        test();
    }
}
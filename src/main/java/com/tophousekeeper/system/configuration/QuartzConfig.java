package com.tophousekeeper.system.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/10/30 17:10
 */
@Configuration
@ImportResource(locations= {"classpath:quarz-bean.xml"})
public class QuartzConfig {
}

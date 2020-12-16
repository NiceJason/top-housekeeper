package com.tophousekeeper.system.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author NiceBin
 * @description: 读取计划任务job的配置
 * @date 2019/10/30 17:10
 */
@Configuration
@ImportResource(locations= {"classpath:xml-configuration/job-config.xml"})
public class JobConfig {
}

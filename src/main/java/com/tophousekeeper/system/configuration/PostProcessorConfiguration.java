package com.tophousekeeper.system.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/5/23 17:22
 */
@Configuration
@ImportResource("/xml-configuration/PostProcessorConfiguration.xml")
public class PostProcessorConfiguration{
}

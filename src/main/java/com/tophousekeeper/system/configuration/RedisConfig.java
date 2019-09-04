//package com.tophousekeeper.system.configuration;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//
///**
// * @author NiceBin
// * @description: TODO
// * @date 2019/9/4 19:44
// */
//
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    @ConfigurationProperties(prefix = "application.properties")
//    JedisConnectionFactory jedisConnectionFactory() {
//        return new JedisConnectionFactory();
//    }
//}

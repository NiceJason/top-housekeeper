package com.tophousekeeper.system.configuration;

import com.alibaba.fastjson.parser.ParserConfig;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.util.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NiceBin
 * @description: CacheManager初始化，使用RedisCacheManager
 *               根据SystemStaticValue中的SystemCache枚举内容进行Cache的注册
 * @date 2019/11/13 17:02
 */
@Configuration
public class CacheConfig {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisCacheManager cacheManager() {

        //创建自定义序列化器
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        //包装成SerializationPair类型
        RedisSerializationContext.SerializationPair serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1))
                .computePrefixWith(cacheName -> "Cache"+cacheName);
        // 针对不同cacheName，设置不同的过期时间
        Map<String, RedisCacheConfiguration> initialCacheConfiguration = new HashMap<String, RedisCacheConfiguration>() {{
            SystemStaticValue.SystemCache[] systemCaches = SystemStaticValue.SystemCache.values();
            Arrays.asList(systemCaches).forEach((systemCache)->
                    put(systemCache.getCacheName(),RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(systemCache.getSurviveTime()))
                    .serializeValuesWith(serializationPair)));
        }};

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig) // 默认配置（强烈建议配置上）。  比如动态创建出来的都会走此默认配置
                .withInitialCacheConfigurations(initialCacheConfiguration) // 不同cache的个性化配置
                .build();

        //设置白名单---非常重要********
        /*
        使用fastjson的时候：序列化时将class信息写入，反解析的时候，
        fastjson默认情况下会开启autoType的检查，相当于一个白名单检查，
        如果序列化信息中的类路径不在autoType中，
        反解析就会报com.alibaba.fastjson.JSONException: autoType is not support的异常
        可参考 https://blog.csdn.net/u012240455/article/details/80538540
        */
        ParserConfig.getGlobalInstance().addAccept("com.tophousekeeper");
        return redisCacheManager;
    }
}

package com.tophousekeeper.system.annotation;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

/**
 * @author NiceBin
 * @description: 系统的缓存注解，包含@Cacheable注解
 *               为了能灵活配置缓存过期时间（配置了就会自动刷新，会在该时间加上随机值1-5分钟，防止同一时间缓存大类过期）
 *               最小过期时间配置CACHE_MIN_OVERDUE
 *               最小强制刷新时间配置CACHE_REFRESH
 * @date 2019/11/18 8:56
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Cacheable
public @interface UpdateCache {
}

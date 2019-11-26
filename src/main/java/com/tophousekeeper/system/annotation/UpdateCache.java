package com.tophousekeeper.system.annotation;

import java.lang.annotation.*;

/**
 * @author NiceBin
 * @description: 缓存更新接口，在Cache实现类的get方法上注解即可
 * @date 2019/11/18 8:56
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface UpdateCache {
}

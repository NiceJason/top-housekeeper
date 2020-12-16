package com.tophousekeeper.system.annotation;

/**
 * 动态修改被@Cacheable修饰的key值
 * 起因是想将登录信息根据账户id存入redis
 * 而id是要@Cacheable
 */
//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface DynamicCacheKey {
//    //将要存到Redis的类路径，可能是基本类的包装类，也可能是自定义类
//    String className();
//}

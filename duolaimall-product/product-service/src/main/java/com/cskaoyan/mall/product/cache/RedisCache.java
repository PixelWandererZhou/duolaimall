package com.cskaoyan.mall.product.cache;

import java.lang.annotation.*;

@Target(ElementType.METHOD)     // 表示这个注解作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 表示这个注解运行的时候生效

// @Documented     // 表示会给这个注解生成JDK文档
// @Inherited         // 表示这个注解是否能被继承
public @interface RedisCache {

    // 定义属性
    String prefix();
}

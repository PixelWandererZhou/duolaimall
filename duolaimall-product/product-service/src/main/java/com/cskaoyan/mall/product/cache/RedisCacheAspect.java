package com.cskaoyan.mall.product.cache;

import com.cskaoyan.mall.common.constant.RedisConst;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RedisCacheAspect {

    @Autowired
    RedissonClient redissonClient;

    /**
     * 在下面的方法中，需要对目标方法进行增强
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.cskaoyan.mall.product.cache.RedisCache)")
    public Object redisCacheAspect(ProceedingJoinPoint joinPoint){

        // 0. 获取相关的参数
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // 获取方法的参数
        Object[] args = joinPoint.getArgs();
        // 获取方法的返回值
        Class returnType = methodSignature.getReturnType();
        // 获取方法上面的注解对象
        RedisCache redisCache = methodSignature.getMethod().getAnnotation(RedisCache.class);
        String prefix = redisCache.prefix();

        // 1. 从Redis中查询数据
        // 那么key是什么呢？ 方法维度 + 参数维度
        String key = prefix + Arrays.asList(args).toString();
        RBucket<Object> bucket = redissonClient.getBucket(key);
        Object obj = bucket.get();

        // 2. 如果查询到了，那么直接返回
       if (obj != null) {
           return obj;
       }

        // 3. 如果没有查询到
        // 4. 加锁
        String lockKey = key + RedisConst.SKULOCK_SUFFIX;
        RLock lock = redissonClient.getLock(lockKey);
        try {


            lock.lock();

            // double check 注意执行之前要 double check）
            obj = bucket.get();
            if (obj != null) {
                return obj;
            }

            // 5. 执行目标方法
            obj = joinPoint.proceed(args);

            // 6. 把从MySQL中查询出的数据，存储到Redis（缓存穿透-----如果为空，要赋默认值）
            if (obj == null) {

                // 如果返回值类型是map
                if (Map.class.equals(returnType)) {
                    obj = new HashMap<>();
                }

                // 如果返回值类型是list
                else if (List.class.equals(returnType)) {
                    obj = new ArrayList<>();
                }
                else {

                    // 如果返回值类型是普通JavaBean
                    Constructor constructor = returnType.getDeclaredConstructor();
                    constructor.setAccessible(true);        // 给构造方法设置访问权限
                    obj = constructor.newInstance();
                }

                // 对于空对象，设置过期时间3分钟
                bucket.set(obj, 3, TimeUnit.MINUTES);

            }else {
                // 对于非空的对象，设置过期时间 5分钟
                bucket.set(obj, 5, TimeUnit.MINUTES);
            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            // 8. 释放锁
            lock.unlock();
        }
        // 7. 返回
        return obj;
    }
}

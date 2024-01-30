package com.cskaoyan.mall.product.runner;

import com.cskaoyan.mall.common.constant.RedisConst;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 布隆过滤器的使用
 * 1. 初始化
 * 在项目启动的时候，我们需要初始化布隆过滤器
 * 问题：如何在项目启动的时候执行一段我们自己的目标代码呢？
 * a：可以直接写在启动类的 main方法里面（不过这种方式一般不使用，不够优雅，也不好维护）
 * b：使用JDK提供的注解 @PostConstruct, JavaBean被创建的时候就会执行这个类中 被@PostConstruct修饰的方法
 * c: 使用 由Spring提供的接口 （CommandLineRunner | ApplicationRunner），我们只需要实现这两个接口即可
 *
 *
 *
 * 2. 添加元素到布隆过滤器
 * 什么时候添加呢？
 * 可以在创建sku的时候添加
 * 也可以在 上架商品的时候添加（更合理）
 *
 *
 * 3. 判断元素是否在布隆过滤器中
 */

@Component
@Slf4j
public class BloomFilterRunner implements CommandLineRunner {

    @Autowired
    RedissonClient redissonClient;

    // 在run方法中，写我们需要执行的代码即可
    @Override
    public void run(String... args) throws Exception {

        long expectedInsertions = 1000;             // 期待插入的元素个数
        double falseProbability = 0.01;             // 误判率

        // 获取布隆过滤器
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);

        // 初始化
        bloomFilter.tryInit(expectedInsertions, falseProbability);


        // 日志输出
        log.info("布隆过滤器初始化成功... 元素个数:{}, 误判率:{}", expectedInsertions, falseProbability);

    }
}

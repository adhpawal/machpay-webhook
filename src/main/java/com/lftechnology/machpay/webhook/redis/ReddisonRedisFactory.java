package com.lftechnology.machpay.webhook.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class ReddisonRedisFactory {

    private static final Long NON_AVAILABLE_SUBSCRIPTION = 0L;
    public static ReddisonRedisFactory instance;
    public static RedissonClient redissonClient;

    public ReddisonRedisFactory() {
        if(instance ==null){
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("127.0.0.1:6379");
            config.setThreads(1000);
            RedissonClient client = Redisson.create(config);
            redissonClient=client;
        }
    }
    public static ReddisonRedisFactory getInstance() {
        if (instance == null) {
            instance = new ReddisonRedisFactory();
        }
        return instance;
    }

    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }
}

